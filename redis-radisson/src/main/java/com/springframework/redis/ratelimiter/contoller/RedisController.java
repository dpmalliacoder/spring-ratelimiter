package com.springframework.redis.ratelimiter.contoller;

import com.springframework.redis.ratelimiter.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("greeting")
    public String greeting(){
        return redisService.greeting();
    }

    @GetMapping("hello")
    public String hello(){
        return redisService.hello();
    }
}
