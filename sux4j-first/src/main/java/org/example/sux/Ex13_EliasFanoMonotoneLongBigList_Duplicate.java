package org.example.sux;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.sux4j.util.EliasFanoMonotoneLongBigList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// int idx -> long origin_value
public class Ex13_EliasFanoMonotoneLongBigList_Duplicate {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    List<Long> list2 = Arrays.asList(1l, 1l, 2l, 3l, 3l, 4l, 5l, 5l, 6l, 7l, 7l, 8l);

    {
      EliasFanoMonotoneLongBigList eList = new EliasFanoMonotoneLongBigList(new LongArrayList(list2));
      System.out.println(String.format(
          "origin bit = %d\n" +
              "eliasFanoMonotone bit = %d",
          list2.size() * 8 * 8, eList.numBits()));

      for (int i = 0; i < list2.size(); i++)
        System.out.println(eList.getLong(i));

      Utils.writeFile("duplicate.obj", eList);
    }

    System.out.println();

    {
      EliasFanoMonotoneLongBigList eList2 = (EliasFanoMonotoneLongBigList) Utils.readFile("duplicate.obj");
      for (int i = 0; i < list2.size(); i++)
        System.out.println(eList2.getLong(i));
    }
  }
}
