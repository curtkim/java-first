package org.example.sux;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.sux4j.util.EliasFanoMonotoneLongBigList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// int idx -> long origin_value
public class Ex12_EliasFanoMonotoneLongBigList {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    // Delta Encoding이랑 비슷한가?
    List<Long> list = Arrays.asList(
        974583489L,
        937459233L,
        827394728L,
        765273615L
    );

    {
      // list가 정렬되어 있지 않으면 Exception이 발생한다.
      EliasFanoMonotoneLongBigList eList = new EliasFanoMonotoneLongBigList(new LongArrayList(list));
      System.out.println(String.format(
          "origin bit = %d\n" +
          "eliasFanoMonotone bit = %d",
          list.size() * 8 * 8, eList.numBits()));

      for (Long a : list)
        System.out.println(a + " " + eList.getLong(a));
    }

  }
}
