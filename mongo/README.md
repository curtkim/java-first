## mongo
- https://www.baeldung.com/spring-data-mongodb-index-annotations-converter
- https://stackoverflow.com/questions/43966601/spring-data-mongodb-how-to-assign-expiration-time-programatically

## redis
- https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-data-redis/src/test/java/com/baeldung/spring/data/redis/repo/StudentRepositoryIntegrationTest.java

## kafka
- https://www.baeldung.com/spring-kafka

## test
- https://www.baeldung.com/spring-boot-testing
- https://github.com/eugenp/tutorials/blob/master/spring-boot/src/test/java/org/baeldung/demo/boottest/EmployeeControllerIntegrationTest.java

## jackson
- https://www.baeldung.com/jackson-object-mapper-tutorial
- http://gdpotter.com/2017/05/24/custom-spring-mvc-jackson/
- hide field : https://stackoverflow.com/questions/14708386/want-to-hide-some-fields-of-an-object-that-are-being-mapped-to-json-by-jackson

## integration test
- https://lucianomolinari.com/2018/05/02/automating-integration-tests-with-docker-compose-and-makefile/


test

    curl -i -H 'Content-Type: application/json' -X PUT -d '{"name" : "Liam", "species" : "cat", "breed" : "siamese", "foot_size":5.1, "location":{"x":1.1, "y":2.2}}' localhost:8080/pet/1
    curl -i localhost:8080/pet/1

    curl -i localhost:8080/pet/box?minX=0&minY=0&maxX=10&maxY=10