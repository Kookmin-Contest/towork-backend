package com.backend.towork.oauth.userinfo;

import com.backend.towork.oauth.provider.OauthProvider;
import com.backend.towork.oauth.userinfo.type.GoogleOauthUser;
import com.backend.towork.oauth.userinfo.type.KakaoOauthUser;
import com.backend.towork.oauth.userinfo.type.NaverOauthUser;

import java.util.Map;

public class OauthUserInfoFactory {
    public static OauthUserInfo getOauthUserInfo(OauthProvider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> new GoogleOauthUser(provider, attributes);
            case NAVER -> new NaverOauthUser(provider, attributes);
            case KAKAO -> new KakaoOauthUser(provider, attributes);
            default -> throw new IllegalArgumentException("Invalid Provider Type");
        };
    }
}
