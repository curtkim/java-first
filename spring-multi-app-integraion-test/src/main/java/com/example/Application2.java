package com.example;

import com.example.big.BigObject;
import com.example.config.BigConfig;
import com.example.config.Service1Config;
import com.example.service.Service1;
import com.example.service.Service2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

// bigObject를 load하지 않아야 한다.
@SpringBootApplication
@ComponentScan(excludeFilters = {
    @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = {Application1.class, Service1Config.class, BigConfig.class})
})
public class Application2 implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(Application2.class, args);
  }

  @Autowired
  private Service2 service2;

  @Override
  public void run(String... args) throws Exception {
    System.out.println(service2.get());
  }
}
