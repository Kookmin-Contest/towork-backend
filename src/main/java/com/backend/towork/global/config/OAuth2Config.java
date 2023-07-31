package com.backend.towork.global.config;

import com.backend.towork.oauth.setup.InMemoryProviderRepository;
import com.backend.towork.oauth.setup.OauthAdapter;
import com.backend.towork.oauth.setup.OauthProperties;
import com.backend.towork.oauth.setup.OauthProviderRegistration;
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
