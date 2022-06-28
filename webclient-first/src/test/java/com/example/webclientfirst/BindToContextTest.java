package com.example.webclientfirst;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
public class BindToContextTest {

  @Autowired
  TempController tempController;


  @Test
  public void bindToController(){
    WebTestClient.bindToController(tempController).build()
        .get().uri("/test")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class).isEqualTo("test");
  }

  @Autowired
  ApplicationContext context;

  @Test
  public void bindToContext(){
    WebTestClient.bindToApplicationContext(context).build()
        .get().uri("/test")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class).isEqualTo("test");
  }
}
