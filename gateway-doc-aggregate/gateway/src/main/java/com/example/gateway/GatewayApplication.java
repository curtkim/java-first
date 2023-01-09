package com.example.gateway;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GatewayApplication {

  //  @Bean
//  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//    return builder.routes()
//        .route(p -> p.path("/book/**").uri("http://localhost:8081"))
//        .route(p -> p.path("/board/**").uri("http://localhost:8082"))
//        .route(p -> p
//            .path("/get")
//            .filters(f -> f.addRequestHeader("Hello", "World"))
//            .uri("http://httpbin.org"))
//        .build();
//  }

  @Bean
  public List<GroupedOpenApi> apis(RouteDefinitionLocator locator) {
    List<GroupedOpenApi> groups = new ArrayList<>();
    List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
    definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
      String name = routeDefinition.getId().replaceAll("-service", "");
      GroupedOpenApi api = GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
      groups.add(api);
    });
    return groups;
  }

    @Bean
    public CommandLineRunner openApiGroups(RouteDefinitionLocator locator, SwaggerUiConfigParameters swaggerUiParameters) {
        return args -> locator
                .getRouteDefinitions().collectList().block()
                .stream()
                .map(RouteDefinition::getId)
                .forEach(swaggerUiParameters::addGroup);
    }

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }

}
