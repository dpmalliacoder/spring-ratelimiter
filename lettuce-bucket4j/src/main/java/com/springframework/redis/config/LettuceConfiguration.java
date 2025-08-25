package com.springframework.redis.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;;
import static java.time.Duration.ofSeconds;

@Configuration
public class LettuceConfiguration {
    public RedisClient redisClient(@Value("${spring.data.redis.host") String host,
                                   @Value("${spring.data.redis.port") String port) {
        return RedisClient.create("redis://password@%s:%s/".formatted(host, port));
    }

    @Bean
    public LettuceBasedProxyManager<byte[]> proxyManager(RedisClient redisClient){
        return Bucket4jLettuce.casBasedBuilder(redisClient)
                .expirationAfterWrite(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(ofSeconds(10)))
                .build();
    }
}
