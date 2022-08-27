package com.example.jackson;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

  @RequestMapping(value="/foo/{id}")
  public Foo getFoo(@PathVariable long id) {
    return new Foo(id, "no name");
  }
}
