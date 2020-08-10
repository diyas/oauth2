package com.fp.oauth2.configuration;

import com.fp.oauth2.configuration.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;


@Configuration
@EnableRedisHttpSession
public class RedisSessionConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(redisProperties.getMinIdle());
        jedisPoolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(redisProperties.getMinEvictableIdleTimeMillis()).toMillis());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(redisProperties.getTimeBetweenEvictionRunsMillis()).toMillis());
        jedisPoolConfig.setBlockWhenExhausted(redisProperties.isBlockWhenExhausted());
        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        //redisStandaloneConfiguration.setDatabase(0);
        //redisStandaloneConfiguration.setPassword(RedisPassword.of("123456"));
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpccb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        jpccb.poolConfig(jedisPoolConfig());
        JedisClientConfiguration jedisClientConfiguration = jpccb.build();
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);

    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return redisTemplate;
    }

}
