package com.springframework.redis.filter;

import com.springframework.redis.service.RateLimitBucketService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestFilter extends OncePerRequestFilter {

    @Autowired
    RateLimitBucketService rateLimitBucketService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().startsWith("/v1")){
            String tenantId = request.getHeader("X-Tenant");

            if(tenantId != null) {
                var bucket = rateLimitBucketService.bucket(tenantId);
                var probe = bucket.tryConsumeAndReturnRemaining(1);

                if(probe.isConsumed()){
                    filterChain.doFilter(request, response);
                }
                else {
                    sendErrorResponse(response, HttpStatus.TOO_MANY_REQUESTS.value());
                }
            }
            else {
                sendErrorResponse(response, HttpStatus.TOO_MANY_REQUESTS.value());
            }
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int value) {
        response.setStatus(value);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE
        );
    }
}
