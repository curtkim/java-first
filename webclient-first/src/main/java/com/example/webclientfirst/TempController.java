package com.example.webclientfirst;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TempController {

  @GetMapping("/test")
  public Mono<String> test(){
    return Mono.just("test");
  }
}
