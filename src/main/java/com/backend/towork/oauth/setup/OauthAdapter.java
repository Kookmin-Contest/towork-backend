package com.backend.towork.oauth.setup;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class OauthAdapter {

    private OauthAdapter() {}
    public static Map<String, OauthProviderRegistration> getOAuth2Providers(OauthProperties properties) {
        Map<String, OauthProviderRegistration> oauthProvider = new HashMap<>();

        properties.getUser().forEach((key, value) -> oauthProvider.put(
                key, new OauthProviderRegistration(value, properties.getProvider().get(key)))
        );

        return oauthProvider;
    }
}
