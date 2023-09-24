package com.towork.api.oauth.userinfo.type;

import com.towork.api.oauth.provider.OauthProvider;
import com.towork.api.oauth.userinfo.OauthUserInfo;

import java.util.Map;

public class GoogleOauthUser extends OauthUserInfo {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_URL = "picture";

    public GoogleOauthUser(OauthProvider provider, Map<String, Object> attributes) {
        super(provider, attributes);
    }
    @Override
    public String getEmail() {
        return (String) this.attributes.get(KEY_EMAIL);
    }
    @Override
    public String getName() {
        return (String) this.attributes.get(KEY_NAME);
    }

    @Override
    public String getImageUrl() {
        return (String) this.attributes.get(KEY_IMAGE_URL);
    }
    @Override
    public Map<String, Object> getExtraData() {
        return this.attributes;
    }
}
