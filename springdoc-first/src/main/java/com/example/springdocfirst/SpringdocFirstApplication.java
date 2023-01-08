package com.example.springdocfirst;

import com.example.springdocfirst.board.BoardItem;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringdocFirstApplication {

  @Bean
  // by path
  public GroupedOpenApi bookOpenApi() {
    return GroupedOpenApi.builder()
        .group("book").pathsToMatch(new String[]{"/api/book/**"})
        .build();
  }

  @Bean
  // by packages
  public GroupedOpenApi boardOpenApi() {
    return GroupedOpenApi.builder()
        .group("board").packagesToScan(new String[]{BoardItem.class.getPackageName()})
        //.pathsToMatch(new String[]{"/api/board/**"})
        .build();
  }


  public static void main(String[] args) {
    SpringApplication.run(SpringdocFirstApplication.class, args);
  }

}
