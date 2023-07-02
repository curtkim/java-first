package com.example.redisscript;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class VariargTest {

  public static List<String> toStringList(Object... arr) {
    return Arrays.stream(arr).map(it -> it.toString()).collect(Collectors.toList());
  }


  @Test
  public void test() {
    String head = "hello";
    List<String> tail = Arrays.asList("world", "!!!");
    List<String> list = toStringList(head, tail.toArray(new String[]{}));
    assertEquals(2, list.size());

    List<String> all = new ArrayList<>();
    all.add(head);
    all.addAll(tail);
    List<String> list2 = toStringList(all.toArray(new String[]{}));
    assertEquals(Arrays.asList(head, tail.get(0), tail.get(1)), list2);
  }


}
