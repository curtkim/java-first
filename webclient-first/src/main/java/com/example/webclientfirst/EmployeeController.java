package com.example.webclientfirst;

import org.springframework.web.bind.annotation.*;

@RequestMapping("/employee")
@RestController
public class EmployeeController {

  @PostMapping("/")
  public Employee create(Employee employee){
    return employee;
  }

  @PostMapping("/{id}")
  public Employee createWithId(String id, Employee employee){
    return employee;
  }

  @GetMapping("/{id}")
  public Employee get(@PathVariable String id) {
    return new Employee("bob", 20);
  }
}
