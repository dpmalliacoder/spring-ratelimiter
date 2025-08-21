package com.springframework.redis.ratelimiter.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.api.RedissonClient;

@Configuration
public class RedissonConfiguration {
    @Bean
    public RedissonClient redissonClient(@Value("${spring.data.redis.host}") String host,
                                         @Value("${spring.data.redis.port}") int port){
        String address = String.format("redis://%s:%d", host, port);
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setRetryAttempts(5);

        return Redisson.create(config);
    }
}
