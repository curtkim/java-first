## HOWTO

    gradle bootRun
    PROPERTY_HELLO=123 java -jar build/libs/springboot-first-0.0.1-SNAPSHOT.jar

## Multiple main class

    java -cp build/libs/springboot-first-0.0.1-SNAPSHOT.jar -Dloader.main=world.Application org.springframework.boot.loader.PropertiesLauncher