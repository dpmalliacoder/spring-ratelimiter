package com.springframework.redis.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.serialization.Mapper;
import io.github.bucket4j.redis.redisson.Bucket4jRedisson;
import io.github.bucket4j.redis.redisson.cas.RedissonBasedProxyManager;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.time.Duration.ofSeconds;

@Configuration
public class RedissonConfiguration {

    @Bean
    public Redisson redisson(@Value("${spring.data.redis.host}") String host,
                                         @Value("${spring.data.redis.port}") int port){
        String address = String.format("redis://%s:%d", host, port);
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setRetryAttempts(5);

        return ((Redisson)Redisson.create(config));
    }

    @Bean
    public RedissonBasedProxyManager<String> proxyManager (Redisson redisson){
        return Bucket4jRedisson.casBasedBuilder(redisson.getCommandExecutor())
                .expirationAfterWrite(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(ofSeconds(10)))
                .keyMapper(Mapper.STRING)
                .build();
    }
}
