package com.backend.towork.oauth2.userinfo;

import com.backend.towork.oauth2.provider.OAuth2Provider;

import java.util.Collections;
import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;
    protected OAuth2Provider provider;
    public OAuth2UserInfo(OAuth2Provider provider, Map<String, Object> attributes) {
        this.provider = provider;
        this.attributes = attributes;
    }
    public OAuth2Provider getProvider() {
        return provider;
    }
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public abstract String getEmail();
    public abstract Map<String, Object>  getDataContainingEmail();

}