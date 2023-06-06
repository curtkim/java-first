package sux;

import it.unimi.dsi.bits.HuTuckerTransformationStrategy;
import it.unimi.dsi.sux4j.io.BucketedHashStore;
import it.unimi.dsi.sux4j.mph.GOVMinimalPerfectHashFunction;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Ex01_GOVMinimalPerfectHashFunction {

  public static void main(String[] args) throws IOException {
    List<String> list = Arrays.asList(
      "A", "B", "C", "D"
    );

    GOVMinimalPerfectHashFunction.Builder<String> builder = new GOVMinimalPerfectHashFunction.Builder<>();
    GOVMinimalPerfectHashFunction<String> fun = builder
        .keys(list)
        .transform(new HuTuckerTransformationStrategy(list, true))
        .build();

    for(String str : list)
      System.out.println(str + " " + fun.getLong(str));
  }
}
