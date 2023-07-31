package com.backend.towork.oauth.userinfo;

import com.backend.towork.oauth.provider.OauthProvider;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Map;

@AllArgsConstructor
public abstract class OauthUserInfo {
    protected OauthProvider provider;
    protected Map<String, Object> attributes;

    public OauthProvider getProvider() {
        return provider;
    }
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public abstract String getEmail();
    public abstract String getName();

    public abstract String getImageUrl();
    public abstract Map<String, Object> getExtraData();

    /*
    public abstract String getBirth();
    */

    /*
    public abstract String getPhoneNumber();
    */
}
