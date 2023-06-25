package com.example;

import com.example.service.Service1;
import com.example.service.Service2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {Application2.class})
class Application2Tests {

  @Autowired
  Service2 service2;

  @Test
  void contextLoads() {
    assertEquals("Ab", service2.get());
  }

}
