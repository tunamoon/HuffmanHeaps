
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.HashSet;
import java.util.*;

public class BinaryMinHeapImplTest {

    //test size method
    @Test
    public void testSize() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        assertEquals(0, a.size());
    }

    //test empty
    @Test
    public void testEmpty() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        assertTrue(a.isEmpty());
    }

    //test check value - normal value
    @Test
    public void testContainsValue() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        assertFalse(a.containsValue("a"));
        a.add(1, "b");
        assertTrue(a.containsValue("b"));
    }


    //test add - null key IllegalArgument
    @Test(expected = IllegalArgumentException.class)
    public void testAddNull() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(null, "b");
    }

    //test add - value is already in min heap IllegalArgument
    @Test(expected = IllegalArgumentException.class)
    public void testAddRepeatedValue() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(1, "b");
        a.add(5, "b");
    }


    //test add - normal
    @Test
    public void testAddNoChange() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(1, "b");
        a.add(5, "c");
        a.add(2, "d");

        Set<String> set = new HashSet<>(Arrays.asList("c", "b", "d"));

        assertEquals(set, a.values());

    }

    @Test
    public void testAddShiftNodes() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(1, "b");
        a.add(5, "c");
        a.add(2, "d");
        a.add(3, "e");
        a.add(4, "f");

        Set<String> set = new HashSet<>(Arrays.asList("c", "b", "d", "e", "f"));
        assertEquals(set, a.values());
        BinaryMinHeapImpl.Entry<Integer, String> e1 = new BinaryMinHeap.Entry<>(1, "b");
        assertEquals(a.extractMin().key, e1.key);

        BinaryMinHeapImpl.Entry<Integer, String> e2 = new BinaryMinHeap.Entry<>(2, "d");
        assertEquals(a.extractMin().key, e2.key);

        BinaryMinHeapImpl.Entry<Integer, String> e3 = new BinaryMinHeap.Entry<>(3, "e");
        assertEquals(a.extractMin().key, e3.key);

        BinaryMinHeapImpl.Entry<Integer, String> e4 = new BinaryMinHeap.Entry<>(4, "f");
        assertEquals(a.extractMin().key, e4.key);

        BinaryMinHeapImpl.Entry<Integer, String> e5 = new BinaryMinHeap.Entry<>(5, "c");
        assertEquals(a.extractMin().key, e5.key);

        assertEquals(0, a.size());

    }

    //test decrease Key - NoSuchElementException if value is not in heap

    @Test (expected = NoSuchElementException.class)
    public void testDecreaseKeyValueNotFound() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(1, "b");
        a.add(5, "c");
        a.add(2, "d");

        a.decreaseKey("e", 3);

    }


    //test decrease Key - IllegalArgumentException if newKey is null/newKey > key(value)

    @Test (expected = IllegalArgumentException.class)
    public void testDecreaseKeyValueNullKey() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(1, "b");
        a.add(5, "c");
        a.add(2, "d");

        a.decreaseKey("c", null);

    }

    @Test (expected = IllegalArgumentException.class)
    public void testDecreaseKeyKeyTooBig() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(1, "b");
        a.add(5, "c");
        a.add(2, "d");

        a.decreaseKey("c", 5);

    }

    //test decrease key - check if updated key

    @Test
    public void testDecreaseFirstKeyUpdated() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(5, "b");
        a.add(7, "c");
        a.add(10, "d");

        int i = a.peek().key;

        assertEquals(5, i);

        a.decreaseKey("b", 2);

        i = a.peek().key;

        assertEquals(2, i);

    }

    @Test
    public void testDecreaseOtherKeyUpdatedSwitch() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(5, "b");
        a.add(7, "c");
        a.add(10, "d");

        a.decreaseKey("c", 3);


        int i = a.peek().key;
        assertEquals(3, i);
    }

    @Test
    public void testDecreaseOtherKeyUpdated() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(5, "b");
        a.add(7, "c");
        a.add(10, "d");

        a.decreaseKey("c", 6);

        int i = a.peek().key;

        assertEquals(5, i);
        a.extractMin();

        i = a.peek().key;
        assertEquals(6, i);
    }

    //test peek - empty = no such element
    @Test (expected = NoSuchElementException.class)
    public void testPeekEmpty() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();

        a.peek();
    }

    //test peek - normal
    @Test
    public void testPeek() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(5, "b");
        a.add(7, "c");
        a.add(10, "d");

        int i = a.peek().key;
        assertEquals(5, i);
        a.extractMin();
        i = a.peek().key;
        assertEquals(7, i);

        a.extractMin();
        i = a.peek().key;
        assertEquals(10, i);
    }

    //test extractmin - empty = no such element
    @Test (expected = NoSuchElementException.class)
    public void testExtractMinEmpty() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.extractMin();
    }

    //test extractmin = normal
    @Test
    public void testExtractMin() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(1, "b");
        a.add(5, "c");
        a.add(2, "d");

        BinaryMinHeapImpl.Entry<Integer, String> bEntry = a.extractMin();
        BinaryMinHeapImpl.Entry<Integer, String> cEntry = new BinaryMinHeap.Entry<>(1, "b");
        assertEquals(bEntry.key, cEntry.key);

        BinaryMinHeapImpl.Entry<Integer, String> dEntry = a.extractMin();
        BinaryMinHeapImpl.Entry<Integer, String> eEntry = new BinaryMinHeap.Entry<>(2, "d");
        assertEquals(dEntry.key, eEntry.key);

    }

    @Test
    public void testExtractMinSort() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(5, "c");
        a.add(1, "b");
        a.add(2, "d");
        a.add(4, "e");

        BinaryMinHeapImpl.Entry<Integer, String> bEntry = a.extractMin();
        BinaryMinHeapImpl.Entry<Integer, String> cEntry = new BinaryMinHeap.Entry<>(1, "b");
        assertEquals(bEntry.key, cEntry.key);

        BinaryMinHeapImpl.Entry<Integer, String> dEntry = a.extractMin();
        BinaryMinHeapImpl.Entry<Integer, String> eEntry = new BinaryMinHeap.Entry<>(2, "d");
        assertEquals(dEntry.key, eEntry.key);

    }

    @Test
    public void testExtractMinThree() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(5, "c");
        a.add(1, "b");
        a.add(2, "d");
        a.add(4, "e");
        a.add(10, "h");
        a.add(11, "l");
        a.add(12, "o");

        BinaryMinHeapImpl.Entry<Integer, String> bEntry = a.extractMin();
        BinaryMinHeapImpl.Entry<Integer, String> cEntry = new BinaryMinHeap.Entry<>(1, "b");
        assertEquals(bEntry.key, cEntry.key);

        BinaryMinHeapImpl.Entry<Integer, String> dEntry = a.extractMin();
        BinaryMinHeapImpl.Entry<Integer, String> eEntry = new BinaryMinHeap.Entry<>(2, "d");
        assertEquals(dEntry.key, eEntry.key);

        BinaryMinHeapImpl.Entry<Integer, String> fEntry = a.extractMin();
        BinaryMinHeapImpl.Entry<Integer, String> gEntry = new BinaryMinHeap.Entry<>(4, "e");
        assertEquals(gEntry.key, fEntry.key);

        BinaryMinHeapImpl.Entry<Integer, String> hEntry = a.extractMin();
        BinaryMinHeapImpl.Entry<Integer, String> iEntry = new BinaryMinHeap.Entry<>(5, "c");
        assertEquals(hEntry.key, iEntry.key);

        BinaryMinHeapImpl.Entry<Integer, String> jEntry = a.extractMin();
        BinaryMinHeapImpl.Entry<Integer, String> kEntry = new BinaryMinHeap.Entry<>(10, "h");
        assertEquals(jEntry.key, kEntry.key);

    }

    //test set - get all values in the heap
    @Test
    public void testSet() {
        BinaryMinHeapImpl<Integer, String> a = new BinaryMinHeapImpl<>();
        a.add(5, "c");
        a.add(1, "b");
        a.add(2, "d");
        a.add(4, "e");
        a.add(10, "h");
        a.add(11, "l");
        a.add(12, "o");

        assertTrue(a.values().contains("c"));
        assertTrue(a.values().contains("b"));
        assertTrue(a.values().contains("d"));
        assertTrue(a.values().contains("e"));
        assertTrue(a.values().contains("h"));
        assertTrue(a.values().contains("l"));
        assertTrue(a.values().contains("o"));

        a.extractMin();
        assertFalse(a.values().contains("b"));


    }

}
