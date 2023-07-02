package com.example.redisscript;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class RedisScriptApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RedisScriptApplication.class, args);
    }

    @Autowired
    EchoService echoService;

    @Autowired
    LockService lockService;

    @Autowired
    MyService myService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(echoService.echo("hello"));

        System.out.println(lockService.acquire("id1"));

        System.out.println(lockService.doit("id2", "test"));

        boolean result = myService.doit("a", "A", "b", Arrays.asList("B", "C"));
        System.out.println(result);
    }
}
