package org.example.sux;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.sux4j.util.EliasFanoMonotoneLongBigList;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

// int idx -> long origin_value
public class Ex13_EliasFanoMonotoneLongBigList {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    // Delta Encoding이랑 비슷한가?
    List<Long> list = Utils.makeRandomSortedList(4_000_000, 10_000_000_000L);
    int idx = 99;

    {
      EliasFanoMonotoneLongBigList eList = new EliasFanoMonotoneLongBigList(new LongArrayList(list));
      System.out.println(list.get(idx));
      System.out.println(eList.getLong(idx));

      Utils.writeFile("nodeId.obj", eList);

      System.out.println(String.format(
          "origin bytes = %d\n" +
          "eliasFanoMonotone bytes = %d\n" +
          "ratio= %f",
          list.size() * 8 ,
          eList.numBits()/8,
          list.size()*8.0 / eList.numBits()/8));
      System.out.println(Files.size(Path.of("nodeId.obj")) + " bytes <- file");
    }

    {
      EliasFanoMonotoneLongBigList eList2 = (EliasFanoMonotoneLongBigList) Utils.readFile("nodeId.obj");
      System.out.println(eList2.getLong(idx));
    }
  }
}
