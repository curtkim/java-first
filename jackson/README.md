## test

    curl localhost:8080/foo/1
    {"id":1,"name":"no name"}

    curl -H 'Accept: application/json' localhost:8080/foo/1
    {"id":1,"name":"no name"}

    curl -H 'Accept: application/taxi-json' localhost:8080/foo/1
    {"name":"no name","identity":1}

## note
- Accept header로 결정된다.
- HttpMessageConverter Bean의 등록순서에 영향을 받는 것 같다.
