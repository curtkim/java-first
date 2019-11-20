## Note
- @DataMongoTest 돌아가기 위해서는 @SpringBootApplication 이 필요하다. 

## Reference

    integration test : https://www.baeldung.com/spring-boot-embedded-mongodb
    
## Howto

    docker-compose up -d
    gradle bootRun
    gradle test // unit test
    gradle clean itest // unit + integration test
    