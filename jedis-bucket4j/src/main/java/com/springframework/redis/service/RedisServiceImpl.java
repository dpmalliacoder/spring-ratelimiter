package com.springframework.redis.service;

import com.springframework.redis.aspect.RateLimiting;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    private static final String name = "Horst";

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
        return String.format("Hello %s!", name);
    }
}
