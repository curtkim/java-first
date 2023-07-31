package com.example.protobufspringswagger;

import com.baeldung.protobuf.BaeldungTraining.Course;

import java.util.Map;

public class CourseRepository {
  Map<Integer, Course> courses;

  public CourseRepository (Map<Integer, Course> courses) {
    this.courses = courses;
  }

  public Course getCourse(int id) {
    return courses.get(id);
  }

  public Course saveCourse(Course origin){
    int id = courses.size()+1;
    Course course = Course.newBuilder(origin).setId(id).build();
    courses.put(id, course);
    return course;
  }
}
