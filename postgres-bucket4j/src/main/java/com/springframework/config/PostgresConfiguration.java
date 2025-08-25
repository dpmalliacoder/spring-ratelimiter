package com.springframework.config;


import io.github.bucket4j.postgresql.Bucket4jPostgreSQL;
import io.github.bucket4j.postgresql.PostgreSQLSelectForUpdateBasedProxyManager;
import io.github.bucket4j.postgresql.PostgreSQLadvisoryLockBasedProxyManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import java.time.Duration;

import static io.github.bucket4j.distributed.ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax;

@Configuration
public class PostgresConfiguration {

    @Bean
    public PostgreSQLSelectForUpdateBasedProxyManager<Long> proxyManager(DataSource dataSource){
        return Bucket4jPostgreSQL
                .selectForUpdateBasedBuilder(dataSource)
                .expirationAfterWrite(basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(60)))
                .build();



    }

//    @Bean
//    public PostgreSQLadvisoryLockBasedProxyManager<Long> proxyManager(DataSource dataSource) {
//        return Bucket4jPostgreSQL
//                .advisoryLockBasedBuilder(dataSource)
//                .build();
//    }

}
