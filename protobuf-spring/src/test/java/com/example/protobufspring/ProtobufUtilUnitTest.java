package com.example.protobufspring;

import org.junit.jupiter.api.Test;

import com.google.protobuf.Message;

import java.io.IOException;

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
}
