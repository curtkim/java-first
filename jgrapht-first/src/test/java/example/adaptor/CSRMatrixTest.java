package example.adaptor;

import org.jgrapht.alg.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSRMatrixTest {

  @Test
  public void test() {
    CSRMatrix m = new CSRMatrix(4, 5, Arrays.asList(
        new Pair<>(0, 0),
        new Pair<>(0, 3),
        new Pair<>(1, 2),
        new Pair<>(1, 4),
        new Pair<>(2, 1),
        new Pair<>(3, 2),
        new Pair<>(3, 4)
    ));

    assertEquals(new HashSet<>(Arrays.asList(0, 3)), m.nonZerosSet(0));
    assertEquals(new HashSet<>(Arrays.asList(2, 4)), m.nonZerosSet(1));
    assertEquals(new HashSet<>(Arrays.asList(1)), m.nonZerosSet(2));
  }
}
