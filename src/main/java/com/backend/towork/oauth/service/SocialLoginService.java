package com.backend.towork.oauth.service;

import com.backend.towork.jwt.domain.RefreshToken;
import com.backend.towork.jwt.repository.RefreshTokenRepository;
import com.backend.towork.jwt.utils.JwtTokenProvider;
import com.backend.towork.member.domain.dto.response.OauthTokenResponseDto;
import com.backend.towork.member.domain.dto.response.TokenResponseDto;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.entity.Role;
import com.backend.towork.member.repository.MemberRepository;
import com.backend.towork.oauth.provider.OauthProvider;
import com.backend.towork.oauth.setup.InMemoryProviderRepository;
import com.backend.towork.oauth.setup.OauthProviderRegistration;
import com.backend.towork.oauth.userinfo.OauthUserInfo;
import com.backend.towork.oauth.userinfo.OauthUserInfoFactory;
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
            throw new IllegalArgumentException("Invalid Provider with Id: " + registrationId);
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
            throw new IllegalArgumentException("Invalid Provider with Id:" + registrationId);
        }
    }

    public TokenResponseDto login(String registrationId, String code) {
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

        // 신규 회원이면 디비에 저장
        String email = userInfo.getEmail();
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            member = Member.builder()
                    .email(email)
                    .name(userInfo.getName())
                    .password(UUID.randomUUID().toString())
                    .role(Role.USER)
                    .build();
            memberRepository.save(member);
        }
        else {
            throw new IllegalStateException("동일한 이메일을 가진 아이디가 있습니다.");
        }

        String accessToken = jwtTokenProvider.generateToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .username(email)
                        .refreshToken(refreshToken)
                        .build()
        );

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

//    @SuppressWarnings({"unchecked"})
//    private OauthUserInfo getGoogleUserInfo(String authToken) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//        headers.add("Authorization", "Bearer" + authToken);
//        Map<String, Object> attributes = restTemplate.postForObject(
//                "https://www.googleapis.com/oauth2/v3/userinfo",
//                new HttpEntity<>(headers),
//                Map.class
//        );
//        return OauthUserInfoFactory.getOauthUserInfo(OauthProvider.GOOGLE, attributes);
//    }

//    @SuppressWarnings({"unchecked"})
//    private OauthUserInfo getKakaoUserInfo(String authToken) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + authToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//        Map<String, Object> attributes = restTemplate.postForObject(
//                "https://kapi.kakao.com/v2/user/me",
//                new HttpEntity<>(headers),
//                Map.class
//        );
//        return OauthUserInfoFactory.getOauthUserInfo(OauthProvider.KAKAO, attributes);
//    }
}
