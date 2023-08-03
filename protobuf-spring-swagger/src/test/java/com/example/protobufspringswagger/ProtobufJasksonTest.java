package com.example.protobufspringswagger;

import com.baeldung.protobuf.BaeldungTraining;
import org.curioswitch.common.protobuf.json.MessageMarshaller;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProtobufJasksonTest {
  private static final MessageMarshaller marshaller = MessageMarshaller.builder()
      .register(BaeldungTraining.Course.getDefaultInstance())
      .register(BaeldungTraining.CourseList.getDefaultInstance())
      .build();

  BaeldungTraining.Course make(){
    BaeldungTraining.Course course1 = BaeldungTraining.Course.newBuilder()
        .setId(1)
        .setCourseName("REST with Spring")
        .addAllStudent(CourseMother.createTestStudents())
        .build();
    return course1;
  }

  @Test
  public void test() throws IOException {
    BaeldungTraining.Course course1 =make();
    String json = marshaller.writeValueAsString(course1);
    //System.out.println(json);

    BaeldungTraining.Course.Builder builder = BaeldungTraining.Course.newBuilder();
    marshaller.mergeValue(json, builder);
    BaeldungTraining.Course course2 = builder.build();
    assertEquals(course2, course1);
  }

  @Test
  public void testList() throws IOException {
    BaeldungTraining.CourseList list = BaeldungTraining.CourseList.newBuilder().addList(make()).addList(make()).build();
    String json = marshaller.writeValueAsString(list);
    System.out.println(json);
  }
}
