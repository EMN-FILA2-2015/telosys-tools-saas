package org.telosystools.saas.config;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.telosystools.saas.security.security.xauth.TokenProvider;

/**
* Configures x-auth-token security.
*/
@Configuration
public class XAuthConfiguration implements EnvironmentAware {

    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "authentication.xauth.");
    }

    @Bean
    public TokenProvider tokenProvider(){
        String secret = propertyResolver.getProperty("secret", String.class, "mySecretXAuthSecret");
        int validityInSeconds = propertyResolver.getProperty("tokenValidityInSeconds", Integer.class, 3600);
        return new TokenProvider(secret, validityInSeconds);
    }
}
