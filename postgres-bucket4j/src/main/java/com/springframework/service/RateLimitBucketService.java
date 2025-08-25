package com.springframework.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.postgresql.PostgreSQLSelectForUpdateBasedProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

@Component
public class RateLimitBucketService {

    Logger logger = LoggerFactory.getLogger(RateLimitBucketService.class);
    @Autowired
    PostgreSQLSelectForUpdateBasedProxyManager<Long> proxyManager;

    public Bucket bucket(){
        Long key = 1L;
        BucketConfiguration bucketConfiguration = BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(2).refillGreedy(10, ofMinutes(1)))
                .build();
        return proxyManager.getProxy(key, () -> bucketConfiguration);
    }

    private static final int MAX_TO_REMOVE_IN_ONE_TRANSACTION = 1_000;
    private static final int THRESHOLD_TO_CONTINUE_REMOVING = 50;

    @Scheduled(cron = "0 30 4 * * *")
    public void scheduleFixedDelayTask() {
        int removedKeysCount = 0, removedCount;
        do {
            removedCount = proxyManager.removeExpired(MAX_TO_REMOVE_IN_ONE_TRANSACTION);
            if (removedKeysCount > 0) {
                logger.info("Removed {} expired buckets", removedCount);
            } else {
                logger.info("There are no expired buckets to remove");
            }
        } while (removedCount > THRESHOLD_TO_CONTINUE_REMOVING);
    }
}
