package org.example.sux;

import it.unimi.dsi.bits.TransformationStrategies;
import it.unimi.dsi.sux4j.mph.GOVMinimalPerfectHashFunction;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// origin_value -> idx(순차적이지는 않음)
public class Ex02_GOVMinimalPerfectHashFunction {

  public static void main(String[] args) throws IOException {
    List<Long> list = Arrays.asList(
        974583489L,
        937459233L,
        827394728L,
        765273615L
    );

    {
      GOVMinimalPerfectHashFunction.Builder<Long> builder = new GOVMinimalPerfectHashFunction.Builder<>();
      GOVMinimalPerfectHashFunction<Long> fun = builder
          .keys(list)
          .transform(TransformationStrategies.fixedLong())
          .build();

      System.out.println("numBits=" + fun.numBits());
      System.out.println("size64=" + fun.size64());
      for (Long a : list)
        System.out.println(a + " " + fun.getLong(a));
    }

    {
      List<Long> sorted = list.stream().sorted().collect(Collectors.toList());
      GOVMinimalPerfectHashFunction.Builder<Long> builder = new GOVMinimalPerfectHashFunction.Builder<>();
      GOVMinimalPerfectHashFunction<Long> fun = builder
          .keys(sorted)
          .transform(TransformationStrategies.fixedLong())
          .build();

      for (Long a : sorted)
        System.out.println(a + " -> " + fun.getLong(a));
    }
    /*
    {
      Set<Long> set = new HashSet<>();
      for (int i = 0; i < 100; i++) {
        long v = (long) (Math.random() * 10_000_000);
        set.add(v);
      }

      GOVMinimalPerfectHashFunction.Builder<Long> builder = new GOVMinimalPerfectHashFunction.Builder<>();
      GOVMinimalPerfectHashFunction<Long> fun = builder
          .keys(set)
          .transform(TransformationStrategies.fixedLong())
          .build();

      for(long a : set)
        System.out.println(a + " " + fun.getLong(a));
    }
    */
  }
}
