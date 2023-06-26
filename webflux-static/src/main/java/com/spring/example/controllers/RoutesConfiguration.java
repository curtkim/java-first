package com.spring.example.controllers;

import com.spring.example.models.Person;
import com.spring.example.repositories.PersonRespository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RoutesConfiguration {
  @Bean
  RouterFunction<ServerResponse> staticResourceRouter() {
    return RouterFunctions.resources("/**", new FileSystemResource("public/"));
  }

  @Bean
  RouterFunction<?> routes(PersonRespository personRespository) {
    return nest(path("/person"),

        route(RequestPredicates.GET("/{id}"),
            request -> ok().body(personRespository.findById(request.pathVariable("id")), Person.class))

            .andRoute(method(HttpMethod.POST),
                request -> {
                  personRespository.insert(request.bodyToMono(Person.class)).subscribe();
                  return ok().build();
                })
    );
  }
}
