package com.example;

import com.example.service.Service1;
import com.example.service.Service2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(excludeFilters = {
   @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = {Application2.class})
})
public class Application1 implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(Application1.class, args);
  }

  @Autowired
  private Service1 service1;

  @Override
  public void run(String... args) throws Exception {
    System.out.println(service1.get());
  }

}
