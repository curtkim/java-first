package com.example.protobufspringswagger;

import com.baeldung.protobuf.BaeldungTraining;
import com.baeldung.protobuf.BaeldungTraining.Course;
import com.baeldung.protobuf.BaeldungTraining.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import com.innogames.springfox_protobuf.ProtobufPropertiesModule;
import org.curioswitch.common.protobuf.json.MessageMarshaller;
import org.curioswitch.common.protobuf.json.MessageMarshallerModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;

import java.util.*;

@SpringBootApplication
public class ProtobufSpringSwaggerApplication {

  /*
  @Bean
  public ObjectMapper objectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new ProtobufPropertiesModule());
    objectMapper.registerModule(new ProtobufModule());
    return objectMapper;
  }

  @Bean
  // Protobof Message <-> json
  ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter(){
    return new ProtobufJsonFormatHttpMessageConverter();
  }

  */

  @Bean
  public ObjectMapper objectMapper(){
    MessageMarshaller marshaller = MessageMarshaller.builder()
        .register(BaeldungTraining.Course.getDefaultInstance())
        .register(BaeldungTraining.CourseList.getDefaultInstance())
        .build();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(MessageMarshallerModule.of(marshaller));
    return mapper;
  }

  @Bean
  ProtobufHttpMessageConverter protobufHttpMessageConverter() {
    return new ProtobufHttpMessageConverter();
  }

  @Bean
  public CourseRepository createTestCourses() {
    Map<Integer, Course> courses = new HashMap<>();
    Course course1 = Course.newBuilder()
        .setId(1)
        .setCourseName("REST with Spring")
        .addAllStudent(CourseMother.createTestStudents())
        .build();
    Course course2 = Course.newBuilder()
        .setId(2)
        .setCourseName("Learn Spring Security")
        .addAllStudent(new ArrayList<Student>())
        .build();
    courses.put(course1.getId(), course1);
    courses.put(course2.getId(), course2);
    return new CourseRepository(courses);
  }


  public static void main(String[] args) {
    SpringApplication.run(ProtobufSpringSwaggerApplication.class, args);
  }
}
