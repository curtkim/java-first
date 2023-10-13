package com.example.resourceserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

  @GetMapping("/public")
  public String getPublic(){
    return "public message";
  }

  @GetMapping("/protected")
  public String getProtected(){
    return "protected message";
  }

  @GetMapping("/admin")
  public String getAdmin(){
    return "admin message";
  }
}
