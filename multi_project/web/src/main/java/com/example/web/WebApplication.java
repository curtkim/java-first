package com.example.web;

import com.example.JsonMaker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebApplication {

  @Bean
  public JsonMaker jsonMaker(){
    return new JsonMaker();
  }

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

}
