package com.example.webclientfirst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest
public class EmployeeControllerTest {

  @Autowired
  private EmployeeController controller;

  private WebTestClient webTestClient;

  @BeforeEach
  public void setup(){
    webTestClient = WebTestClient.bindToController(controller).build();
  }

  @Test
  public void testGet(){
    webTestClient.get().uri("/employee/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody(Employee.class).isEqualTo(new Employee("bob", 20));
  }

  @Test
  public void testCreate(){
    LinkedMultiValueMap map = new LinkedMultiValueMap();
    map.add("name", "alice");
    map.add("age", "19");

    webTestClient.post().uri("/employee/")
        .body(BodyInserters.fromMultipartData(map))
        .exchange()
        .expectStatus().isOk()
        .expectBody(Employee.class).isEqualTo(new Employee("alice", 19));
  }

  @Test
  public void testCreateWithId(){
    LinkedMultiValueMap map = new LinkedMultiValueMap();
    map.add("name", "alice");
    map.add("age", "19");

    webTestClient.post().uri("/employee/2")
        .body(BodyInserters.fromMultipartData(map))
        .exchange()
        .expectStatus().isOk()
        .expectBody(Employee.class).isEqualTo(new Employee("alice", 19));
  }
}
