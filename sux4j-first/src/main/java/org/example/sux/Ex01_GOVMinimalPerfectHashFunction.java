package org.example.sux;

import it.unimi.dsi.bits.HuTuckerTransformationStrategy;
import it.unimi.dsi.sux4j.mph.GOVMinimalPerfectHashFunction;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * GOVMinimalPerfectHashFunction은 순서를 보장하지 않는 것 같다.
 */
public class Ex01_GOVMinimalPerfectHashFunction {

  public static void main(String[] args) throws IOException {
    List<String> list = Arrays.asList(
      "C", "D", "A", "B"
        );

    GOVMinimalPerfectHashFunction.Builder<String> builder = new GOVMinimalPerfectHashFunction.Builder<>();
    GOVMinimalPerfectHashFunction<String> fun = builder
        .keys(list)
        .transform(new HuTuckerTransformationStrategy(list, true))
        .build();

    System.out.println("numBits=" + fun.numBits());

    for(String str : list)
      System.out.println(str + " " + fun.getLong(str));
  }
}
