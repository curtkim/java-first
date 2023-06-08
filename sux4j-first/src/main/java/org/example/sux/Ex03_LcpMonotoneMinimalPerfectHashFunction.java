package org.example.sux;

import it.unimi.dsi.bits.TransformationStrategies;
import it.unimi.dsi.sux4j.mph.GOVMinimalPerfectHashFunction;
import it.unimi.dsi.sux4j.mph.LcpMonotoneMinimalPerfectHashFunction;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// origin_value -> idx(순차적이지는 않음)
public class Ex03_LcpMonotoneMinimalPerfectHashFunction {

  public static void main(String[] args) throws IOException {

    List<Long> list = Utils.makeRandomSortedList(3_000_000, 10_000_000_000L);
    {
      LcpMonotoneMinimalPerfectHashFunction.Builder<Long> builder = new LcpMonotoneMinimalPerfectHashFunction.Builder<>();
      LcpMonotoneMinimalPerfectHashFunction<Long> fun = builder
          .keys(list)
          .transform(TransformationStrategies.fixedLong())
          .build();

      System.out.println("numBits=" + fun.numBits());
      System.out.println("numBytes=" + fun.numBits()/8);
      System.out.println("size64=" + fun.size64());
      for (Long a : list.subList(0, 10))
        System.out.println(a + " " + fun.getLong(a));


      //Utils.writeFile("lcp.obj", fun);
    }
  }
}
