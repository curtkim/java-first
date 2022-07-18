package com.example.redistransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class MyService {

  @Autowired
  private MyDao1 myDao1;

  @Autowired
  private MyDao2 myDao2;

  @Transactional
  public void doit(){
    myDao1.doit1();
    myDao2.doit2();
  }

}
