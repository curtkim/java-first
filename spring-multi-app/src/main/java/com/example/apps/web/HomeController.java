package com.example.apps.web;

import com.example.service.HelloService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HomeController {

  HelloService helloService;

  @RequestMapping("/")
  public String hello(){
    return helloService.hi();
  }
}
