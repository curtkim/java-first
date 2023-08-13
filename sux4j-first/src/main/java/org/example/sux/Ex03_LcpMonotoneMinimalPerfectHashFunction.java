package org.example.sux;

import it.unimi.dsi.bits.TransformationStrategies;
import it.unimi.dsi.sux4j.mph.LcpMonotoneMinimalPerfectHashFunction;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

// origin_value -> idx(순차적임)
public class Ex03_LcpMonotoneMinimalPerfectHashFunction {

  public static void main(String[] args) throws IOException, ClassNotFoundException {

    List<Long> list = Utils.makeRandomSortedList(3_000_000, 10_000_000_000L);

    {
      LcpMonotoneMinimalPerfectHashFunction.Builder<Long> builder = new LcpMonotoneMinimalPerfectHashFunction.Builder<>();
      LcpMonotoneMinimalPerfectHashFunction<Long> fun = builder
          .keys(list)
          .transform(TransformationStrategies.fixedLong())
          .build();

      System.out.println("numBits=" + fun.numBits());
      System.out.println("numBytes=" + fun.numBits() / 8);
      System.out.println("size64=" + fun.size64());
      for (Long a : list.subList(0, 10))
        System.out.println(a + " " + fun.getLong(a));

      Utils.writeFile("lcp.obj", fun);
      Utils.writeFile("lcp_origin.obj", (Serializable)list);
    }

    {
      LcpMonotoneMinimalPerfectHashFunction<Long> fun = (LcpMonotoneMinimalPerfectHashFunction<Long>) Utils.readFile("lcp.obj");
      for (Long a : list.subList(0, 10))
        System.out.println(a + " " + fun.getLong(a));
    }
  }
}
