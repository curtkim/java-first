# reference
https://www.javainuse.com/spring/boot-jwt

## howto

    curl -X POST localhost:8080/authenticate -H 'Content-Type: application/json' -d '{"username":"javainuse","password":"password"}'
    curl -H 'Authorization: Bearer ???' localhost:8080/hello