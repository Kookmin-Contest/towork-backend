package com.backend.towork.oauth2.userinfo;


import com.backend.towork.oauth2.provider.OAuth2Provider;
import com.backend.towork.oauth2.userinfo.type.GoogleOAuth2User;
import com.backend.towork.oauth2.userinfo.type.KakaoOAuth2User;
import com.backend.towork.oauth2.userinfo.type.NaverOAuth2User;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(OAuth2Provider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> new GoogleOAuth2User(provider, attributes);
            case NAVER -> new NaverOAuth2User(provider, attributes);
            case KAKAO -> new KakaoOAuth2User(provider, attributes);
            default -> throw new IllegalArgumentException("Invalid Provider Type.");
        };
    }
}
