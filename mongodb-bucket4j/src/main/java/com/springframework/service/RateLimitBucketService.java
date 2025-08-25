package com.springframework.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.mongodb_async.Bucket4jMongoDBAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.time.Duration.ofMinutes;

@Component
public class RateLimitBucketService {

    Logger logger = LoggerFactory.getLogger(RateLimitBucketService.class);
    @Autowired
    Bucket4jMongoDBAsync.MongoDBAsyncCompareAndSwapBasedProxyManagerBuilder<String> proxyManager;

    public Bucket bucket(String key){
        BucketConfiguration bucketConfiguration = BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(2).refillGreedy(10, ofMinutes(1)))
                .build();
        return proxyManager.build().getProxy(key, () -> bucketConfiguration);
    }

}
