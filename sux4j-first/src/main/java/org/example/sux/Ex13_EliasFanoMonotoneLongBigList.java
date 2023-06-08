package org.example.sux;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.sux4j.util.EliasFanoMonotoneLongBigList;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

// int idx -> long origin_value
public class Ex13_EliasFanoMonotoneLongBigList {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    // Delta Encoding이랑 비슷한가?
    List<Long> list = Utils.makeRandomSortedList(100, 10_000_000_000L);
    int idx = 99;

    {
      EliasFanoMonotoneLongBigList eList = new EliasFanoMonotoneLongBigList(new LongArrayList(list));
      System.out.println(String.format(
          "origin bit = %d\n" +
          "eliasFanoMonotone bit = %d",
          list.size() * 8 * 8, eList.numBits()));

      System.out.println(list.get(idx));
      System.out.println(eList.getLong(idx));

      Utils.writeFile("nodeId.obj", eList);
    }

    {
      EliasFanoMonotoneLongBigList eList2 = (EliasFanoMonotoneLongBigList) Utils.readFile("nodeId.obj");
      System.out.println(eList2.getLong(idx));
    }
  }
}
