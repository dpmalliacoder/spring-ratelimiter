package com.springframework.aspect;

import com.springframework.service.RateLimitBucketService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RateLimitingAspect {
    private final RateLimitBucketService rateLimitBucketService;

    public RateLimitingAspect(RateLimitBucketService rateLimitBucketService) {
        this.rateLimitBucketService = rateLimitBucketService;
    }

    @Around("@annotation(com.springframework.redis.aspect.RateLimiting)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimiting annotation = method.getAnnotation(RateLimiting.class);

        String name = annotation.name();
        String fallbackName = annotation.fallbackMethodName();
        var bucket = rateLimitBucketService.bucket();
        var probe = bucket.tryConsumeAndReturnRemaining(1);

        if(probe.isConsumed()){
            return joinPoint.proceed();
        }
        else {
            return invokeFallback(joinPoint, fallbackName);
        }
    }

    private Object invokeFallback(ProceedingJoinPoint joinPoint, String fallbackName) throws Exception{
        Object target = joinPoint.getTarget();
        Method fallbackMethod = target.getClass().getMethod(fallbackName);

        return fallbackMethod.invoke(target);
    }
}
