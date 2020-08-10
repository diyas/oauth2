package com.fp.oauth2.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
@Order(0)
public class RedisProperties {
    private String host;
    private int port;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private int minEvictableIdleTimeMillis;
    private int timeBetweenEvictionRunsMillis;
    private boolean blockWhenExhausted;
    private boolean usePool;
}
