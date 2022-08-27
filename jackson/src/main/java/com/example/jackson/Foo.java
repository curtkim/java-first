package com.example.jackson;

public class Foo {
  long id;
  String name;

  public Foo(long id, String name) {
    this.id =id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
