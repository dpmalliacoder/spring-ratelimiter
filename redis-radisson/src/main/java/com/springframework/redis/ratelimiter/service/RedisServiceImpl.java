package com.springframework.redis.ratelimiter.service;

import com.springframework.redis.ratelimiter.aspect.RateLimiting;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

@Service
public class RedisServiceImpl implements RedisService{

    private final RateLimitingService rateLimitingService;
    private static final String name = "Horst";

    public RedisServiceImpl(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @RateLimiting(
            name=name,
            fallbackMethodName = "greetingFallback"
    )
    @Override
    public String greeting() {
        return String.format("Hello %s!", name);
    }

    public String greetingFallback(){
        return String.format("You are not welcome %s!", name);

    }

    public String hello(){
        CompletionStage<String> result = rateLimitingService.resolveRateLimiter(name)
                .tryAcquireAsync()
                .thenApplyAsync( acquired -> {
                    if (acquired) {
                        return String.format("Hello %s!", name);
                    }
                    else {
                        return greetingFallback();
                    }
                });

        return result.toCompletableFuture().join();
    }
}
