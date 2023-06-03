package org.example;

import java.util.Map;

public class MapOf {

  public static void main(String[] args) {
    Map<String, String> map = Map.of(
      "key1", "value1",
      "key2", "value2",
      "key3", "value3"
    );
    System.out.println(map);
  }
}
