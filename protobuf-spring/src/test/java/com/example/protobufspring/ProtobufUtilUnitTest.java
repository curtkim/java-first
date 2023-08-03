package com.example.protobufspring;

import com.baeldung.protobuf.BaeldungTraining;
import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;

import com.google.protobuf.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProtobufUtilUnitTest {

  public static String jsonStr = "{\r\n"
      + "  \"boolean\": true,\r\n"
      + "  \"color\": \"gold\",\r\n"
      + "  \"object\": {\r\n"
      + "    \"a\": \"b\",\r\n"
      + "    \"c\": \"d\"\r\n"
      + "  },\r\n"
      + "  \"string\": \"Hello World\"\r\n"
      + "}";

  @Test
  public void givenJson_convertToProtobuf() throws IOException {
    Message protobuf = ProtobufUtil.fromJson(jsonStr);
    assertTrue(protobuf.toString().contains("key: \"boolean\""));
    assertTrue(protobuf.toString().contains("string_value: \"Hello World\""));
  }

  @Test
  public void givenProtobuf_convertToJson() throws IOException {
    Message protobuf = ProtobufUtil.fromJson(jsonStr);
    String json = ProtobufUtil.toJson(protobuf);
    assertTrue(json.contains("\"boolean\": true"));
    assertTrue(json.contains("\"string\": \"Hello World\""));
    assertTrue(json.contains("\"color\": \"gold\""));
  }


  @Test
  public void jsonFormat_with_custom_builder() throws IOException {
    BaeldungTraining.Course course1 = BaeldungTraining.Course.newBuilder()
        .setId(1)
        .setCourseName("REST with Spring")
        .addAllStudent(ProtobufSpringApplication.createTestStudents())
        .build();

    String json = ProtobufUtil.toJson(course1);
    System.out.println(json);

    BaeldungTraining.Course.Builder builder = BaeldungTraining.Course.newBuilder();
    JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
    BaeldungTraining.Course course2 = builder.build();

    System.out.println("=========");
    System.out.println(course2);
    assertEquals(course1, course2);
  }

}
