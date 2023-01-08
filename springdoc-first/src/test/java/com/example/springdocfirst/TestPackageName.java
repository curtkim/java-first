package com.example.springdocfirst;

import com.example.springdocfirst.board.BoardItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPackageName {

  @Test
  public void test(){
    assertEquals("com.example.springdocfirst.board", BoardItem.class.getPackageName());
  }
}
