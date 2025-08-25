package com.springframework.redis.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static java.time.Duration.ofMinutes;

@Component
public class RateLimitBucketService {

    @Autowired
    LettuceBasedProxyManager<byte[]> proxyManager;

    public Bucket bucket(String key){
        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(10).refillGreedy(100, ofMinutes(1)))
                .build();
        return proxyManager.getProxy(key.getBytes(StandardCharsets.UTF_8), () -> configuration);
    }
}
