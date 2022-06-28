package com.example.webclientfirst;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

public class BindToFunctionTest {

  @Test
  public void bindToRouterFunction(){
    RouterFunction function = RouterFunctions.route(
        RequestPredicates.GET("/resource"),
        request -> ServerResponse.ok().build()
    );

    WebTestClient
        .bindToRouterFunction(function)
        .build().get().uri("/resource")
        .exchange()
        .expectStatus().isOk()
        .expectBody().isEmpty();
  }

  @Test
  public void bindToWebHandler(){
    WebHandler handler = (ServerWebExchange exchange) -> Mono.empty();
    WebTestClient.bindToWebHandler(handler).build()
        .get()
        .exchange()
        .expectStatus().isOk()
        .expectBody().isEmpty();
  }

}
