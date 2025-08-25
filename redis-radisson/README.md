# Redis-radisson
In this project is not using the Bucket4j libraries. This project use the radisson library to manages the rate limit without bucketing.

There are 2 different approach to control rate limit
- Controlling REST API using filters
- Controlling Function execution

How to execute
- Created Aspect to control the Function 
 - curl -X GET http://localhost:8080/greeting
- Created Filter to control API access
  - curl -X GET 'http://localhost:8080/v1/hello' --header 'X-Tenant: my-key'
