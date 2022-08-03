package com.example.redislockrace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyController {

  @Autowired
  private MyService service;


  @RequestMapping("/{id}/begin")
  public String begin(@PathVariable String id, @RequestParam String value){
    service.begin(id, value);
    return "ok";
  }

  @RequestMapping("/{id}/append")
  public String append(@PathVariable String id, @RequestParam String value) {
    service.append(id, value);
    return "ok";
  }

  @RequestMapping("/{id}/end")
  public String end(@PathVariable String id, @RequestParam String value){
    service.end(id, value);
    return "ok";
  }

  @RequestMapping("/{id}")
  public String get(@PathVariable String id){
    return service.get(id);
  }

  @RequestMapping("/{id}/valid")
  public Boolean isValid(@PathVariable String id){
    return service.isValid(id);
  }
}

