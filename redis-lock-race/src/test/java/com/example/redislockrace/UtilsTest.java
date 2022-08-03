package com.example.redislockrace;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTest {

  @Test
  public void isSorted() {
    assertTrue(Utils.isSorted(Arrays.asList(1,2,3)));
    assertFalse(Utils.isSorted(Arrays.asList(2,1,3)));
    assertFalse(Utils.isSorted(Arrays.asList(3,2,1)));
  }
}
