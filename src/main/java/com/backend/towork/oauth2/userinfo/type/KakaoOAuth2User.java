package com.backend.towork.oauth2.userinfo.type;

import com.backend.towork.oauth2.provider.OAuth2Provider;
import com.backend.towork.oauth2.userinfo.OAuth2UserInfo;

import java.util.Map;

public class KakaoOAuth2User extends OAuth2UserInfo {

    private static final String KEY_EMAIL = "email";
    private static final String KEY_DATA = "kakao_account";

    public KakaoOAuth2User(OAuth2Provider provider, Map<String, Object> attributes) {
        super(provider, attributes);
    }

    @Override
    public String getEmail() {
        return (String) this.getDataContainingEmail().get(KEY_EMAIL);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Map<String, Object> getDataContainingEmail() {
        return (Map<String, Object>) attributes.get(KEY_DATA);
    }
}
