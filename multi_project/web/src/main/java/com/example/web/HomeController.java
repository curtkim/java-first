package com.example.web;

import com.example.JsonMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @Autowired
  private JsonMaker jsonMaker;

  @RequestMapping("/")
  public String home() {
    return jsonMaker.make("ok").toString();
  }
}
