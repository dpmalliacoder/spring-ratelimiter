package com.springframework.redis.ratelimiter.service;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


@Service
public class RateLimitingService {
    @Value("{rateLimit.capacity}") long capacity;
    @Value("{rateLimit.keepAliveTime}") long keepAliveTime;
    @Value("{rateLimit.interval}") long interval;
    @Value("{rateLimit.unit}") String unit;

    private final RedissonClient redissonClient;

    public RateLimitingService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RRateLimiter resolveRateLimiter(String key){
        var rateLimiter = redissonClient.getRateLimiter(key);

        rateLimiter.trySetRate(RateType.OVERALL, capacity,
                Duration.of(interval, ChronoUnit.valueOf(unit.toUpperCase())),
                Duration.of(keepAliveTime, ChronoUnit.valueOf(unit.toUpperCase())));
        return rateLimiter;
    }
}
