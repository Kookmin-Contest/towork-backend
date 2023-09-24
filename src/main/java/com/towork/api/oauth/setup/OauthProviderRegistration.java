package com.towork.api.oauth.setup;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthProviderRegistration {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String tokenUri;
    private final String userInfoUri;
    private final String authorizationUri;
    private final String redirectUriAfter;

    public OauthProviderRegistration(OauthProperties.User user, OauthProperties.Provider provider) {
        this(user.getClientId(), user.getClientSecret(), user.getRedirectUriAfter(), user.getRedirectUri(), provider.getTokenUri(), provider.getUserInfoUri(), provider.getAuthorizationUri());
    }

    @Builder
    public OauthProviderRegistration(String clientId, String clientSecret, String redirectUriAfter, String redirectUri, String tokenUri, String userInfoUri, String authorizationUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
        this.authorizationUri = authorizationUri;
        this.redirectUriAfter = redirectUriAfter;
    }

}
