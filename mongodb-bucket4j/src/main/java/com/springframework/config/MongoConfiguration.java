package com.springframework.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.mongodb_async.Bucket4jMongoDBAsync;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


import static java.time.Duration.ofSeconds;

@Configuration
public class MongoConfiguration {


    @Bean
    public Bucket4jMongoDBAsync.MongoDBAsyncCompareAndSwapBasedProxyManagerBuilder<String> proxyManager(){

        MongoClient mongoClient = MongoClients.create("mongodb://admin:secret@localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("bucket4j");
        MongoCollection<Document> collection = database.getCollection("buckets");

        return Bucket4jMongoDBAsync.compareAndSwapBasedBuilder(collection)
                .expirationAfterWrite(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(ofSeconds(10)));

    }



}
