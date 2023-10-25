import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @param <V>   {@inheritDoc}
 * @param <Key> {@inheritDoc}
 */
public class BinaryMinHeapImpl<Key extends Comparable<Key>, V> implements BinaryMinHeap<Key, V> {
    /**
     * {@inheritDoc}
     */

    private ArrayList<Entry<Key, V>> entries = new ArrayList<Entry<Key, V>>();
    private HashMap<V, Integer> tracker = new HashMap<V, Integer>();

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public boolean isEmpty() {
        return entries.size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(V value) {
        return tracker.containsKey(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Key key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        } else if (containsValue(value)) {
            throw new IllegalArgumentException("already contained value");
        } else {
            //O(logn)
            //value - needed to be updated
            //key - new key value
            tracker.put(value, entries.size());
            entries.add(new Entry<>(key, value));
            decreaseKey(value, key);


        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseKey(V value, Key newKey) {
        //if the newer key is greater than the current key
        if (!containsValue(value)) {
            throw new NoSuchElementException("can not find value");
        } else if (newKey == null || newKey.compareTo(entries.get(entries.size() - 1).key) > 0) {
            throw new IllegalArgumentException("given key too big");
        } else {
            int i = tracker.get(value);
            if (i == 0) {
                entries.set(i, new Entry<Key, V>(newKey, value));
                return;
            }
            //if parent node is greater than child node, switch positions to keep minHeap
            //switch values of map HashMap, too
            while (i > 0 && (entries.get((i - 1) / 2).key.compareTo(newKey) == 1)) {
                tracker.replace(value, i, (i - 1) / 2);
                tracker.replace(entries.get((i - 1) / 2).value, (i - 1) / 2, i);
                Entry<Key, V> sub = entries.get((i - 1) / 2);
                Entry<Key, V> updated = new Entry<>(newKey, value);

                entries.set((i - 1) / 2, updated);
                entries.set(i, sub);

                i = (i - 1) / 2;
            }
            Entry<Key, V> updated = new Entry<>(newKey, value);
            entries.set(i, updated);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry<Key, V> peek() {
        if (entries.size() < 1) {
            throw new NoSuchElementException("heap empty");
        }
        return entries.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry<Key, V> extractMin() {
        if (entries.size() < 1) {
            throw new NoSuchElementException("size too small");
        } else {
            Entry<Key, V> min = entries.get(0);
            tracker.remove(min.value);
            entries.set(0, entries.get(entries.size() - 1));

            entries.remove(entries.size() - 1);
            if (entries.size() == 0) {
                return min;
            } else {
                //always minheapify 0 since that's where we removed the min
                minHeapify(0);
                return min;
            }

        }
    }

    private void minHeapify(int i) {
        //find the key of the left child and right child

        Entry<Key, V> curr = entries.get(i);
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        int min;

        if (l < entries.size() && (entries.get(l).key.compareTo(curr.key) < 0)) {
            min = l;
        } else {
            min = i;
        }
        Entry<Key, V> curr1 = entries.get(min);
        if (r < entries.size() && (entries.get(r).key.compareTo(curr1.key) < 0)) {
            min = r;
        }
        if (min != i) {
            entries.set(i, entries.get(min));
            tracker.replace(entries.get(min).value, min, i);
            entries.set(min, curr);
            tracker.replace(curr.value, i, min);

            minHeapify(min);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> values() {
        Set<V> entrySet = new HashSet<>();
        for (V key : tracker.keySet()) {
            entrySet.add(key);
        }
        return entrySet;

    }
}