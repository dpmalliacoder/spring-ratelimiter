package com.springframework.redis.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.serialization.Mapper;
import io.github.bucket4j.redis.jedis.Bucket4jJedis;
import io.github.bucket4j.redis.jedis.cas.JedisBasedProxyManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

import static java.time.Duration.ofSeconds;

@Configuration
public class JedisConfiguration{
    @Bean
    public JedisPool jedisPool(@Value("${spring.data.redis.host}") String host,
                               @Value("${spring.data.redis.port}") String port) {
        final JedisPoolConfig jedisPoolConfig = buildPoolConfig();
        return new JedisPool(jedisPoolConfig, host, Integer.parseInt(port));
    }

    private JedisPoolConfig buildPoolConfig() {
        var poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleDuration(ofSeconds(60));
        poolConfig.setTimeBetweenEvictionRuns(ofSeconds(30));
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);

        return poolConfig;
    }

    @Bean
    public JedisBasedProxyManager<String> proxyManager(JedisPool jedisPool){
        JedisBasedProxyManager<String> proxyManager = Bucket4jJedis.casBasedBuilder(jedisPool)
                .expirationAfterWrite(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(ofSeconds(10)))
                .keyMapper(Mapper.STRING)
                .build();

        return proxyManager;
    }
}
