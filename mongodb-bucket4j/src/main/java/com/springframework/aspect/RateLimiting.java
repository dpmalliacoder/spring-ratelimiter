package com.springframework.aspect;

import java.lang.annotation.*;

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiting {
    String name();
    String fallbackMethodName() default "";
}
