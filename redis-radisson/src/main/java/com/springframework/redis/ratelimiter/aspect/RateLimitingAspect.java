package com.springframework.redis.ratelimiter.aspect;

import com.springframework.redis.ratelimiter.service.RateLimitingService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RateLimitingAspect {
    private final RateLimitingService rateLimitingService;

    public RateLimitingAspect(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @Around("@annotation(com.springframework.redis.ratelimiter.aspect.RateLimiting)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimiting annotation = method.getAnnotation(RateLimiting.class);

        String name = annotation.name();
        String fallbackName = annotation.fallbackMethodName();
        boolean acquired = rateLimitingService.resolveRateLimiter(name).tryAcquire();

        if(acquired){
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
