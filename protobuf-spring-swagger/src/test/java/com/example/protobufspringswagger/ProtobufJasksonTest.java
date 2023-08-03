package com.example.protobufspringswagger;

import com.baeldung.protobuf.BaeldungTraining;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.curioswitch.common.protobuf.json.MessageMarshaller;
import org.curioswitch.common.protobuf.json.MessageMarshallerModule;
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
  public void usingProtobufRepeat() throws IOException {
    BaeldungTraining.CourseList list = BaeldungTraining.CourseList.newBuilder().addList(make()).addList(make()).build();
    String json = marshaller.writeValueAsString(list);
    System.out.println(json);
  }

  @Test
  // https://github.com/curioswitch/protobuf-jackson/blob/1d5fd929ba5532f9a70aa46af3dffd72776a3487/src/main/java/org/curioswitch/common/protobuf/json/MessageMarshallerModule.java#L18
  public void usingJavaList() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(MessageMarshallerModule.of(marshaller));
    String json = mapper.writeValueAsString(Arrays.asList(make(), make()));
    System.out.println(json);
  }
}
