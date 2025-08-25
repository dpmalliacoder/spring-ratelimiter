# spring-ratelimiter
spring rate limiter

Use case -
-  To protect your system from overloading you want to introduce the following limitation:
The bucket size is 50 calls (which cannot be exceeded at any given time), with a "refill rate" of 10 calls per second 
that continually increases tokens in the bucket. In other words. if the client app averages 10 calls per second, 
it will never be throttled, and moreover, the client has overdraft equals to 50 calls which can be used if the average 
is a little bit higher than 10 calls/sec in a short time period.
- To protect the external Rest API that you are consuming by enabling the rate limiter in your codebase which control 
the rate at which requests are sent or processed to maintain system stability.

# How Rate Limiting Helps
- Rate-limiting can prevent denial of service attacks
- It helps in estimating incoming and outgoing traffic to/from your service.
- It allows you to implement tier-based pricing model, pay for higher rate of request.

# How the Token Bucket Algorithm Work
Token bucket is an algorithm that can use to implement rate limiting.
- A bucket is created with a certain number of token
- when your service receive any request, the bucket is checked. if it has capacity then it allow to proceed, otherwise 
the request is denied.
- On processing each request the bucket capacity count reduces.
- Based on the configuration, the bucket capacity is replenished.

# Bucket4j
- Bucket4j is a Java rate-limiting library that is mainly based on the token-bucket algorithm.
- Bucket4j is good scalable for multi-threading cases it by default uses lock-free implementation.
- Bucket4j provides the framework that allows you to quickly build integration with your own persistent technology like RDMS or key-value storage.


In this project have different modules that describe about the token bucket algorithm in
- Bucket4j-Redis
  - jedis 
  - lettuce
  - radisson

Integrated with RDBMS and NO SQL
- Postgres
- MongoDB

For more information follow the documentation - https://bucket4j.com/8.15.0/toc.html

# Describe an example of a local burst:
- Given a bucket with a limit of 100 tokens/min. We start with a full bucket, i.e. with 100 tokens.
- At T1 100 requests are made and thus the bucket becomes empty.
- At T1+1min the bucket is full again because tokens are fully regenerated, and we can immediately consume 100 tokens.
- This means that between T1 and T1+1min we have consumed 200 tokens. Over a long time, there will be no more than 100 requests per min, but as shown above, it is possible to burst at twice the limit here at 100 tokens per min.