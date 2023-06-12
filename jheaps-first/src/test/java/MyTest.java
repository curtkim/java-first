import org.jheaps.AddressableHeap;
import org.jheaps.array.BinaryArrayIntegerValueHeap;
import org.jheaps.tree.BinaryTreeAddressableHeap;
import org.jheaps.tree.PairingHeap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTest {

  @Test
  public void test(){
    BinaryArrayIntegerValueHeap heap =new BinaryArrayIntegerValueHeap(10);
    heap.insert(1);
    heap.insert(2);
    heap.insert(10);
    assertEquals(1, heap.deleteMin());
    assertEquals(2, heap.deleteMin());
    heap.insert(5);
    assertEquals(5, heap.deleteMin());
    assertEquals(10, heap.deleteMin());
  }

  @Test
  public void testAddressableHeap(){
    AddressableHeap<Integer, String> heap = new BinaryTreeAddressableHeap();

    heap.insert(10, "A");
    heap.insert(20, "B");
    heap.insert(5, "C");

    AddressableHeap.Handle<Integer, String> h = heap.deleteMin();
    //assertEquals("C", h.getKey());
    assertEquals(5, h.getKey());
    assertEquals("C", h.getValue());

    h = heap.deleteMin();
    assertEquals(10, h.getKey());

    h = heap.deleteMin();
    assertEquals(20, h.getKey());
  }


  @Test
  public void testAddressableHeap2(){
    AddressableHeap<Integer, String> heap = new PairingHeap<>();

    AddressableHeap.Handle<Integer, String> h1 = heap.insert(10, "A");
    AddressableHeap.Handle<Integer, String> h2 = heap.insert(20, "B");
    AddressableHeap.Handle<Integer, String> h3 = heap.insert(5, "C");
    h1.decreaseKey(4);

    AddressableHeap.Handle<Integer, String> h = heap.deleteMin();
    assertEquals(4, h.getKey());
    assertEquals("A", h.getValue());

    h = heap.deleteMin();
    assertEquals(5, h.getKey());
    assertEquals("C", h.getValue());

    h = heap.deleteMin();
    assertEquals(20, h.getKey());
    assertEquals("B", h.getValue());
  }
}
