package com.example.apps.cli;

import com.example.service.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

  @Bean
  public HelloService helloService(){
    return new HelloService();
  }
}
