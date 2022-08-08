import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

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
    public void flatmap(){
        assertEquals(
                List.of(1,1,2,2,3,3),
                List.of(1,2,3).flatMap((it)-> List.of(it, it))
        );
    }

    @Test
    public void lastOption(){
       assertTrue(List.of().lastOption().isEmpty());
    }

    @Test
    public void emptyList_last(){
        assertThrows(NoSuchElementException.class, ()-> {
            List.of().last();
        });
    }

    @Test
    public void foreach(){
        List.of(1,2,3).forEach((i-> System.out.println(i)));
    }
}
