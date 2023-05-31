package com.example.springbootwebandcli;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultRunner implements ApplicationRunner {
  @Override
  public void run(ApplicationArguments args) {
    // optionArgs가 없으면 아무것도 안한다.
    if( args.getNonOptionArgs().size() > 0)
      runInner();
  }

  void runInner(){
    System.out.println("Hello, World!");
  }
}
