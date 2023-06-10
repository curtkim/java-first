package example.adaptor;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.stream.IntStream;

public class CompleteIntegerSet extends AbstractSet<Integer> {
  private int n;

  /**
   * Create an integer set from 0 to n-1.
   *
   * @param n the number n
   */
  public CompleteIntegerSet(int n) {
    this.n = n;
  }

  @Override
  public Iterator<Integer> iterator() {
    return IntStream.range(0, n).iterator();
  }

  @Override
  public boolean contains(Object o) {
    if (o instanceof Integer) {
      Integer x = (Integer) o;
      return x >= 0 && x < n;
    }
    return false;
  }

  @Override
  public int size() {
    return n;
  }

}
