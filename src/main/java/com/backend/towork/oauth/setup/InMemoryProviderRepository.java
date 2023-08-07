package com.backend.towork.oauth.setup;

import java.util.HashMap;
import java.util.Map;

public class InMemoryProviderRepository {
    private final Map<String, OauthProviderRegistration> providers;

    public InMemoryProviderRepository(Map<String, OauthProviderRegistration> providers) {
        this.providers = new HashMap<>(providers);
    }

    public OauthProviderRegistration findByProviderName(String name) {
        return providers.get(name);
    }

}
