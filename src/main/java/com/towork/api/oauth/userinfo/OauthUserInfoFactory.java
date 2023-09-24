package com.towork.api.oauth.userinfo;

import com.towork.api.oauth.provider.OauthProvider;
import com.towork.api.oauth.userinfo.type.GoogleOauthUser;
import com.towork.api.oauth.userinfo.type.KakaoOauthUser;
import com.towork.api.oauth.userinfo.type.NaverOauthUser;

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
