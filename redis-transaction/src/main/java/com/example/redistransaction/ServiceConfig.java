package com.example.redistransaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

  @Bean
  MyDao1 myDao1(){
    return new MyDao1();
  }

  @Bean
  MyDao2 myDao2(){
    return new MyDao2();
  }

  @Bean
  MyService myService(){
    return new MyService();
  }
}
