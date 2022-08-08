import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortedSetTest {

    @Test
    public void test_duplicate_entry(){
        SortedSet<Integer> set = TreeSet.of(2, 3, 1, 2);
        assertEquals(3, set.size());
    }
}
