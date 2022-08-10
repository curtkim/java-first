import io.vavr.Tuple;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class ListTest {

    @Test
    public void concat() {
        List a = List.of(1, 2, 3);
        List b = List.of(4, 5, 6);

        assertEquals(List.of(1, 2, 3, 4, 5, 6), a.appendAll(b));
    }

    @Test
    public void addNull() {
        List<Integer> list = List.of(1, 2, null);
        assertNull(list.last());
        assertNull(list.get(2));

        assertEquals(1, list.head());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));

        assertEquals(3, list.length());
        assertEquals(3, list.size());
    }

    @Test
    public void testSum() {
        assertEquals(6, List.of(1, 2, 3).reduce((i, j) -> i + j));
        assertEquals(6L, List.of(1, 2, 3).sum());
    }

    @Test
    public void flatmap() {
        assertEquals(
                List.of(1, 1, 2, 2, 3, 3),
                List.of(1, 2, 3).flatMap((it) -> List.of(it, it))
        );
    }

    @Test
    public void lastOption() {
        assertTrue(List.of().lastOption().isEmpty());
    }

    @Test
    public void emptyList_last() {
        assertThrows(NoSuchElementException.class, () -> {
            List.of().last();
        });
    }

    @Test
    public void foreach() {
        List.of(1, 2, 3).forEach((i -> System.out.println(i)));
    }

    @Test
    public void splitAt() {
        assertEquals(
                Tuple.of(List.of(1, 2, 3, 4, 5), List.of(6, 7, 8, 9, 10)),
                List.rangeClosed(1, 10).splitAt(5)
        );
    }

    @Test
    public void splitAtPredicate() {
        assertEquals(
                Tuple.of(List.of(1, 2, 3), List.of(4, 5, 6, 7, 8, 9, 10)),
                List.rangeClosed(1, 10).splitAt(new DurationPredicate(3))
        );
    }

    @Test
    public void splitAtPredicate_noSplit() {
        assertEquals(
                Tuple.of(
                        List.of(1, 2),
                        List.of()
                ),
                List.rangeClosed(1, 2).splitAt(new DurationPredicate(3))
        );
    }

    class DurationPredicate implements Predicate<Integer> {
        private int duration;
        private Integer first;

        public DurationPredicate(int duration) {
            this.duration = duration;
        }

        @Override
        public boolean test(Integer integer) {
            if (first == null)
                first = integer;

            if (integer - first >= duration)
                return true;

            return false;
        }
    }

    @Test
    public void createList() {
        java.util.List<Integer> javaList = Arrays.asList(1, 2, 3);
        List<Integer> list = List.of(javaList.toArray(new Integer[0]));

        assertEquals(List.of(1, 2, 3), list);
    }

    @Test
    public void slideBy() {
        Iterator<List<Integer>> iter = List.rangeClosed(4, 10).slideBy(x -> x / 3);
        assertEquals( List.of(4,5), iter.next());
        assertEquals( List.of(6,7,8), iter.next());
        assertEquals( List.of(9,10), iter.next());
    }

    @Test
    public void subtract(){
        List a = List.of(1,2,3);
        List b = a.appendAll(List.of(4,5));
        assertEquals(List.of(4,5), b.subSequence(a.size()));
    }
}
