## howto

    curl localhost:8080/courses/1
    # protobuf로 반환

    curl -i -H "Accept: application/x-protobuf" localhost:8080/courses/1
    # protobuf로 반환

    curl -H "Accept: application/json" localhost:8080/courses/1
    # json으로 반환

    curl -H "Accept: application/json" localhost:8080/courses/
    # objectMapper가 필요하다.

    curl -X POST -H 'Content-Type: application/json' -H "Accept: application/json" -d '{"courseName":"math"}' localhost:8080/courses/


## reference
- https://www.baeldung.com/spring-rest-api-with-protocol-buffers
- https://eternalwind.github.io/tech/2022/05/20/Making-springdoc-openapi-works-with-protobuf.html
- https://github.com/EternalWind/springdoc-protobuf-example

## 주의 
- proto파일에서 camel형식을 사용해야 한다.
 