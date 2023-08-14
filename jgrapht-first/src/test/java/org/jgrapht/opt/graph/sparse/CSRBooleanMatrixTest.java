package org.jgrapht.opt.graph.sparse;

import example.Utils;
import org.jgrapht.alg.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSRBooleanMatrixTest {

  @Test
  public void nonZerosSet_reverse() throws IOException, ClassNotFoundException {
    // pair list 순서에 상관이 없다.
    CSRBooleanMatrix m = new CSRBooleanMatrix(3, 3, Arrays.asList(
        new Pair<>(1, 2),
        new Pair<>(0, 2),
        new Pair<>(0, 1)
    ));

    assertEquals(new HashSet<>(Arrays.asList(1, 2)), m.nonZerosSet(0));
    assertEquals(new HashSet<>(Arrays.asList(2)), m.nonZerosSet(1));
    assertEquals(new HashSet<>(), m.nonZerosSet(2));
  }

  @Test
  public void nonZerosSet() throws IOException, ClassNotFoundException {
    CSRBooleanMatrix m = new CSRBooleanMatrix(3, 3, Arrays.asList(
        new Pair<>(0, 1),
        new Pair<>(0, 2),
        new Pair<>(1, 2)
    ));

    assertEquals(new HashSet<>(Arrays.asList(1, 2)), m.nonZerosSet(0));
    assertEquals(new HashSet<>(Arrays.asList(2)), m.nonZerosSet(1));
    assertEquals(new HashSet<>(), m.nonZerosSet(2));

    Utils.writeFile("matrix.obj", m);
    {
      CSRBooleanMatrix m2 = (CSRBooleanMatrix) Utils.readFile("matrix.obj");

      assertEquals(new HashSet<>(Arrays.asList(1, 2)), m2.nonZerosSet(0));
      assertEquals(new HashSet<>(Arrays.asList(2)), m2.nonZerosSet(1));
      assertEquals(new HashSet<>(), m2.nonZerosSet(2));
    }
  }

  @Test
  public void test() throws IOException, ClassNotFoundException {
    int size = 4_000_000;
    List<Pair<Integer, Integer>> list = makeRandomPairList(size, size, size * 3);
    CSRBooleanMatrix m = new CSRBooleanMatrix(size, size, list);

    Utils.writeFile("matrix2.obj", m);
    long start = System.currentTimeMillis();
    CSRBooleanMatrix m2 = (CSRBooleanMatrix) Utils.readFile("matrix2.obj");
    System.out.println(System.currentTimeMillis() - start);
  }

  static List<Pair<Integer, Integer>> makeRandomPairList(int row, int col, int count) {
    Set<Pair<Integer, Integer>> set = new HashSet<>();

    while (set.size() < count) {
      int a = (int) (Math.random() * row);
      int b = (int) (Math.random() * col);
      set.add(new Pair<>(a, b));
    }
    return new ArrayList<>(set);
  }
}
