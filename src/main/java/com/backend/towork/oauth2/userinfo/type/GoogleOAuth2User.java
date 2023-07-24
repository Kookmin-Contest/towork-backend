package com.backend.towork.oauth2.userinfo.type;

import com.backend.towork.oauth2.provider.OAuth2Provider;
import com.backend.towork.oauth2.userinfo.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2User extends OAuth2UserInfo {
    private static final String KEY_EMAIL = "email";
    public GoogleOAuth2User(OAuth2Provider provider, Map<String, Object> attributes) {
        super(provider, attributes);
    }
    @Override
    public String getEmail() {
        return (String) this.attributes.get(KEY_EMAIL);
    }
    @Override
    public Map<String, Object> getDataContainingEmail() {
        return this.attributes;
    }
}
