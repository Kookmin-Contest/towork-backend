package com.backend.towork.oauth2.service;

import com.backend.towork.member.domain.Member;
import com.backend.towork.member.principal.PrincipalDetails;
import com.backend.towork.oauth2.provider.OAuth2Provider;
import com.backend.towork.oauth2.userinfo.OAuth2UserInfo;
import com.backend.towork.oauth2.userinfo.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialAccountService socialAccountService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                OAuth2Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()), oAuth2User.getAttributes()
        );

        log.info(oAuth2UserInfo.getAttributes().toString());

        Member member = socialAccountService.updateSocialAccount(oAuth2UserInfo);
        return new PrincipalDetails(member, oAuth2UserInfo.getAttributes());
    }
}
