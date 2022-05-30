## Notes
- @DataMongoTest, @DataRedisTest, @WebMvcTest 비슷한 레벨. ApplicationContext를 최소로 구성해서 테스트 한다.
 
## Reference

    integration test : https://www.baeldung.com/spring-boot-embedded-mongodb
    https://jschmitz.dev/posts/how_to_test_the_data_layer_of_your_spring_boot_application_with_dataMongotest/

## Howto

    docker-compose up -d
    gradle bootRun
    gradle test // unit test
    gradle clean itest // unit + integration test

## What is @DataMongoTest
@DataMongoTest is an annotation that is used to test the MongoDB components of our application. 
Applied to a test class it will disable the full-auto-configuration and instead configure only those components, 
that are relevant for MongoDB tests. For example MongoRepository, ReactiveMongoRepository, MongoClient, and MongoTemplate. 
It will also scan our project for classes with the @Documents annotation. 
If there is an embedded in-memory database on the classpath, it will be used for the test.
