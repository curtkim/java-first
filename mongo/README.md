## mongo
- https://www.baeldung.com/spring-data-mongodb-index-annotations-converter

## test
- https://www.baeldung.com/spring-boot-testing
- https://github.com/eugenp/tutorials/blob/master/spring-boot/src/test/java/org/baeldung/demo/boottest/EmployeeControllerIntegrationTest.java

## jackson
- https://www.baeldung.com/jackson-object-mapper-tutorial
- http://gdpotter.com/2017/05/24/custom-spring-mvc-jackson/

## integration test
- https://lucianomolinari.com/2018/05/02/automating-integration-tests-with-docker-compose-and-makefile/


test

    curl -i -H 'Content-Type: application/json' -X PUT -d '{"name" : "Liam", "species" : "cat", "breed" : "siamese", "foot_size":5.1, "location":{"x":1.1, "y":2.2}}' localhost:8080/pet/1
    curl -i localhost:8080/pet/1