package com.example.protobufspringswagger;

import com.baeldung.protobuf.BaeldungTraining.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {

  @Autowired
  CourseRepository courseRepo;

  @GetMapping("/courses/{id}")
  Course get(@PathVariable Integer id) {
    return courseRepo.getCourse(id);
  }

  @PostMapping("/courses/")
  Course post(@RequestBody Course course) {
    return courseRepo.saveCourse(course);
  }

  @GetMapping("/courses/")
  List<Course> get(){
    return courseRepo.getAll();
  }
}
