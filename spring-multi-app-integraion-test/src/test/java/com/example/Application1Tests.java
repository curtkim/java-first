package com.example;

import com.example.service.Service1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {Application1.class})
class Application1Tests {

  @Autowired
  Service1 service1;

  @Test
  void contextLoads() {
    assertEquals("AbBIG", service1.get());
  }

}
