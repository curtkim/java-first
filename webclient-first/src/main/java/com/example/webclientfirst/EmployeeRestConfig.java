package com.example.webclientfirst;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class EmployeeRestConfig {

  @Bean
  RouterFunction<ServerResponse> employeeRoutes() {

  return route(GET("/employee/{id}"),
      (ServerRequest req) -> {
        return ok().body(Mono.just(new Employee("bob", 20)), Employee.class);
      })
      .and(route(POST("/employee/"), req->{
        Mono<Employee> emp = req.body(toMono(Employee.class));
        return ok().body(emp, Employee.class);
      }));
  }
}
