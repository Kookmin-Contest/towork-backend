package com.towork.api.oauth.service;

import com.towork.api.global.error.exception.BusinessException;
import com.towork.api.jwt.domain.RefreshToken;
import com.towork.api.jwt.repository.RefreshTokenRepository;
import com.towork.api.jwt.utils.JwtTokenProvider;
import com.towork.api.member.domain.dto.response.OauthTokenResponseDto;
import com.towork.api.auth.domain.dto.response.TokenResponseDto;
import com.towork.api.member.domain.entity.Member;
import com.towork.api.member.domain.entity.Role;
import com.towork.api.member.repository.MemberRepository;
import com.towork.api.oauth.provider.OauthProvider;
import com.towork.api.oauth.setup.InMemoryProviderRepository;
import com.towork.api.oauth.setup.OauthProviderRegistration;
import com.towork.api.oauth.userinfo.OauthUserInfo;
import com.towork.api.oauth.userinfo.OauthUserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String SCOPE_GOOGLE = "email%20profile";
    private static final String CODE = "code";

    private final InMemoryProviderRepository inMemoryProviderRepository;
    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String getAuthorizationUriRequest(String registrationId) {
        OauthProviderRegistration oAuthProviderRegistration = inMemoryProviderRepository.findByProviderName(registrationId);
        if (oAuthProviderRegistration == null) {
            throw new BusinessException(400, "존재하지 않는 Provider ID 입니다.");
        }
        if (registrationId.equals("google")) {
            return UriComponentsBuilder.fromUriString(oAuthProviderRegistration.getAuthorizationUri())
                    .queryParam("response_type", CODE)
                    .queryParam("client_id", oAuthProviderRegistration.getClientId())
                    .queryParam("scope", SCOPE_GOOGLE)
                    .queryParam("redirect_uri", oAuthProviderRegistration.getRedirectUri())
                    .build().toUriString();
        }
        else if (registrationId.equals("kakao")) {
            return UriComponentsBuilder.fromUriString(oAuthProviderRegistration.getAuthorizationUri())
                    .queryParam("response_type", CODE)
                    .queryParam("client_id", oAuthProviderRegistration.getClientId())
                    .queryParam("redirect_uri", oAuthProviderRegistration.getRedirectUri())
                    .build().toUriString();
        }
        else {
            throw new BusinessException(400, "유효하지 않는 Provider ID 입니다.");
        }
    }

    public TokenResponseDto socialLogin(String registrationId, String code) {
        OauthProviderRegistration provider = inMemoryProviderRepository.findByProviderName(registrationId);

        // 코드로 토큰 가져오기
        OauthTokenResponseDto tokenResponseDto = getToken(provider, code);

        log.info(tokenResponseDto.getAccessToken());
        log.info(tokenResponseDto.getTokenType());
        log.info(tokenResponseDto.getScope());

        // 리소스 서버로부터 유저정보 가져오기
        OauthUserInfo userInfo = OauthUserInfoFactory.getOauthUserInfo(
                OauthProvider.valueOf(registrationId.toUpperCase()),
                getUserAttributes(provider, tokenResponseDto)
        );

        log.info(userInfo.getProvider() + ": " + userInfo.getAttributes() + " ...  user info");

        String email = userInfo.getEmail();
        Member member = memberRepository.findByEmail(email).orElse(null);

        // 가입되지 않은 경우
        if (member == null) {
            member = Member.builder()
                    .email(email)
                    .name(userInfo.getName())
                    .authProvider(userInfo.getProvider().name())
                    .password(UUID.randomUUID().toString())
                    .role(Role.USER)
                    .build();
            memberRepository.save(member);
        }
        // 1. 신규회원인데 이미 등록한 이메일이 DB에 존재하는 경우
        // -> ex) 자체 회원가입(AuthController)을 example@gmail.com으로 했고, 소셜로그인도 example@gmail.com으로 시도 하는 경우
        // -> (authProvider)가 각각 TO-WORK(자체), GOOGLE(userInfo)로 다르다.
        // 2. 구회원인데 그냥 로그인 하는 경우
        // -> 코드 진행
        else {
            if (!member.getAuthProvider().equals(userInfo.getProvider().name())) {
                throw new BusinessException(400, "동일한 이메일이 존재합니다.");
            }
        }

        String accessToken = jwtTokenProvider.generateToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .username(email)
                        .refreshToken(refreshToken)
                        .build()
        );

        log.info("AT: " + accessToken);
        log.info("RT: " + refreshToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Map<String, Object> getUserAttributes(OauthProviderRegistration oAuth2Provider, OauthTokenResponseDto tokenResponseDto) {
        return WebClient.create()
                .get()
                .uri(oAuth2Provider.getUserInfoUri())
                .headers(header -> header.setBearerAuth(tokenResponseDto.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>(){})
                .block();
    }

    private OauthTokenResponseDto getToken(OauthProviderRegistration oAuthProviderRegistration, String code) {
        return WebClient.create()
                .post()
                .uri(oAuthProviderRegistration.getTokenUri())
                .headers(header -> {
                    header.setBasicAuth(oAuthProviderRegistration.getClientId(), oAuthProviderRegistration.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(oAuthProviderRegistration, code))
                .retrieve()
                .bodyToMono(OauthTokenResponseDto.class)
                .block();
    }
    private MultiValueMap<String, String> tokenRequest(OauthProviderRegistration oAuthProviderRegistration, String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", AUTHORIZATION_CODE);
        formData.add("redirect_uri", oAuthProviderRegistration.getRedirectUri());
        formData.add("client_id", oAuthProviderRegistration.getClientId());
        return formData;
    }
}
