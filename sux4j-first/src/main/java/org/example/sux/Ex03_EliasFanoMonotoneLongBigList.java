package org.example.sux;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.sux4j.util.EliasFanoMonotoneLongBigList;

import java.io.*;
import java.util.List;

// int idx -> long origin_value
public class Ex03_EliasFanoMonotoneLongBigList {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    // Delta Encoding이랑 비슷한가?
    List<Long> list = Utils.makeRandomSortedList(100, 10_000_000_000L);
    int idx = 99;

    File file = new File("nodeId.obj");
    {
      EliasFanoMonotoneLongBigList eList = new EliasFanoMonotoneLongBigList(new LongArrayList(list));
      System.out.println(String.format(
          "origin bit = %d\n" +
          "eliasFanoMonotone bit = %d",
          list.size() * 8 * 8, eList.numBits()));

      System.out.println(list.get(idx));
      System.out.println(eList.getLong(idx));

      ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file));
      o.writeObject(eList);
      o.close();
    }

    {
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
      EliasFanoMonotoneLongBigList eList2 = (EliasFanoMonotoneLongBigList) ois.readObject();

      System.out.println(eList2.getLong(idx));
    }
  }
}
