package com.springframework.redis.controller;

import com.springframework.redis.service.RedisService;
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

    @GetMapping("/v1/hello")
    public String hello(){
        return redisService.hello();
    }
}
