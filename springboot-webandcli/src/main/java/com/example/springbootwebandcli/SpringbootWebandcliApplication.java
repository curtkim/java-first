package com.example.springbootwebandcli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootWebandcliApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(SpringbootWebandcliApplication.class);
    if( args.length > 0) {
      // cli
      app.setWebApplicationType(WebApplicationType.NONE);
      app.run(args).close();
    }
    else{
      // web
      // disable CommandLineRunner
      app.run(args);
    }
  }
}
