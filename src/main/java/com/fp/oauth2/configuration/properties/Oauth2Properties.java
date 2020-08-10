package com.fp.oauth2.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Data
@Order(0)
public class Oauth2Properties {
    private String url;
    private String clientId;
    private String clientSecret;
    private int tokenExpired;
    private int refreshTokenExpired;
    private boolean redisStore;

    public String getCredentials() {
        return this.getClientId() + ":" + this.getClientSecret();
    }
}
