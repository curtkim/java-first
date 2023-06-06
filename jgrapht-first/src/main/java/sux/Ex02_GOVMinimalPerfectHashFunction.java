package sux;

import it.unimi.dsi.bits.HuTuckerTransformationStrategy;
import it.unimi.dsi.bits.TransformationStrategies;
import it.unimi.dsi.sux4j.io.BucketedHashStore;
import it.unimi.dsi.sux4j.mph.GOVMinimalPerfectHashFunction;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Ex02_GOVMinimalPerfectHashFunction {

  public static void main(String[] args) throws IOException {
    {
      List<Long> list = Arrays.asList(
          974583489L,
          937459233L,
          827394728L,
          765273615L
      );

      GOVMinimalPerfectHashFunction.Builder<Long> builder = new GOVMinimalPerfectHashFunction.Builder<>();
      GOVMinimalPerfectHashFunction<Long> fun = builder
          .keys(list)
          .transform(TransformationStrategies.fixedLong())
          .build();

      for (Long a : list)
        System.out.println(a + " " + fun.getLong(a));
    }

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
  }
}
