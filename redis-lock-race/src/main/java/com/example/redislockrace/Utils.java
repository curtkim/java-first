package com.example.redislockrace;

import java.util.Iterator;
import java.util.List;

public class Utils {
  public static boolean isSorted(List<Integer> list) {
    Iterator<Integer> iter = list.iterator();
    Integer current, previous = iter.next();
    while (iter.hasNext()) {
      current = iter.next();
      if (previous.compareTo(current) > 0) {
        return false;
      }
      previous = current;
    }
    return true;
  }
}
