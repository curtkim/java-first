package com.example.protobufspring;

import com.baeldung.protobuf.BaeldungTraining;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {
  @Autowired
  CourseRepository courseRepo;

  @RequestMapping("/courses/{id}")
  BaeldungTraining.Course customer(@PathVariable Integer id) {
    return courseRepo.getCourse(id);
  }

  @GetMapping("/courses/")
  List<BaeldungTraining.Course> customerAll(){
    return new ArrayList<>(courseRepo.courses.values());
  }
}
