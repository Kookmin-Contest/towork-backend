package com.backend.towork.oauth.userinfo.type;

import com.backend.towork.oauth.provider.OauthProvider;
import com.backend.towork.oauth.userinfo.OauthUserInfo;

import java.util.Map;

public class KakaoOauthUser extends OauthUserInfo {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "nickname";
    private static final String KEY_IMAGE_URL = "profile_image";
    private static final String KEY_EXTRA_DATA = "kakao_account";
    private static final String KEY_PROPERTIES = "properties";
    public KakaoOauthUser(OauthProvider provider, Map<String, Object> attributes) {
        super(provider, attributes);
    }
    @Override
    public String getEmail() {
        return (String) this.getExtraData().get(KEY_EMAIL);
    }

    @Override
    public String getName() {
        return (String) this.getProperties().get(KEY_NAME);
    }

    @Override
    public String getImageUrl() {
        return (String) this.getProperties().get(KEY_IMAGE_URL);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Map<String, Object> getExtraData() {
        return (Map<String, Object>) this.attributes.get(KEY_EXTRA_DATA);
    }

    @SuppressWarnings({"unchecked"})
    public Map<String, Object> getProperties() {
        return (Map<String, Object>) this.attributes.get(KEY_PROPERTIES);
    }
}
