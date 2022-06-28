package com.example.webclientfirst;

import java.util.Objects;

public class Employee {
  private String name;
  private int age;

  Employee(){
  }

  public Employee(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee employee = (Employee) o;
    return age == employee.age && Objects.equals(name, employee.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age);
  }
}
