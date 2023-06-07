package org.jgrapht.opt.graph.sparse.specifics;

import org.jgrapht.alg.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSRBooleanMatrixTest {

  @Test
  public void nonZerosSet() {
    CSRBooleanMatrix m = new CSRBooleanMatrix(3, 3, Arrays.asList(
        new Pair<>(0, 1),
        new Pair<>(0, 2),
        new Pair<>(1, 2)
    ));

    assertEquals(Set.of(1,2), m.nonZerosSet(0));
    assertEquals(Set.of(2), m.nonZerosSet(1));
    assertEquals(Set.of(), m.nonZerosSet(2));
  }
}
