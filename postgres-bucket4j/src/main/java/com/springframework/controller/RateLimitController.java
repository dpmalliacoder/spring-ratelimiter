package com.springframework.controller;

import com.springframework.service.RateLimitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {
    private final RateLimitService redisService;

    public RateLimitController(RateLimitService redisService) {
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
