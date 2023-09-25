package com.example.springdoc2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @RequestMapping(value = "/health")
  public String health() {
    return "ok";
  }
}
