package com.example;

import io.lettuce.core.cluster.SlotHash;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SlotHashTest {

  @Test
  public void test(){
    String key = "{user101}/drive";
    String key2 = "{user101}/path";
    assertEquals(SlotHash.getSlot(key), SlotHash.getSlot(key2));
  }

  @Test
  public void test2(){
    String key = "{user102}/drive";
    String key2 = "{user101}/path";
    assertNotEquals(SlotHash.getSlot(key), SlotHash.getSlot(key2));
  }
}
