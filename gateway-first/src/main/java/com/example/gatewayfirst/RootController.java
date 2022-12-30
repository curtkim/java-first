package com.example.gatewayfirst;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

  @ResponseBody
  @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
  public String helloWorld() {
    return "hello world";
  }
}
