package com.towork.api.oauth.userinfo.type;

import com.towork.api.oauth.provider.OauthProvider;
import com.towork.api.oauth.userinfo.OauthUserInfo;

import java.util.Map;

public class NaverOauthUser extends OauthUserInfo {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_URL = "profile_image";
    private static final String KEY_EXTRA_DATA = "response";

    public NaverOauthUser(OauthProvider provider, Map<String, Object> attributes) {
        super(provider, attributes);
    }
    @Override
    public String getEmail() {
        return (String) this.getExtraData().get(KEY_EMAIL);
    }
    @Override
    public String getName() {
        return (String) this.getExtraData().get(KEY_NAME);
    }
    @Override
    public String getImageUrl() {
        return (String) this.getExtraData().get(KEY_IMAGE_URL);
    }
    @Override
    @SuppressWarnings({"unchecked"})
    public Map<String, Object> getExtraData() {
        return (Map<String, Object>) this.attributes.get(KEY_EXTRA_DATA);
    }
}
