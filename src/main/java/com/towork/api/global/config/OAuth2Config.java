package com.towork.api.global.config;

import com.towork.api.oauth.setup.InMemoryProviderRepository;
import com.towork.api.oauth.setup.OauthAdapter;
import com.towork.api.oauth.setup.OauthProperties;
import com.towork.api.oauth.setup.OauthProviderRegistration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(OauthProperties.class)
public class OAuth2Config {
    private final OauthProperties properties;

    public OAuth2Config(OauthProperties properties) {
        this.properties = properties;
    }

    @Bean
    public InMemoryProviderRepository inMemoryProvideRepository() {
        Map<String, OauthProviderRegistration> providers = OauthAdapter.getOAuth2Providers(properties);
        return new InMemoryProviderRepository(providers);
    }
}
