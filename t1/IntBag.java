import java.util.Iterator;
import java.util.NoSuchElementException;

/******************************************************************************
 *  IntBag — bolsa especializada para int primitivo, adaptada a partir de
 *  Bag.java (algs4). Guarda os elementos num array redimensionável (int[])
 *  em vez de lista ligada de Integer, evitando autoboxing e uma alocação de
 *  objeto por elemento — essencial para grades grandes (até ~2.000.000 de
 *  arestas no pior caso do Counting Rooms).
 ******************************************************************************/
public class IntBag implements Iterable<Integer> {
    private int[] items;
    private int n;

    public IntBag() {
        items = new int[4];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void add(int item) {
        if (n == items.length) {
            int[] novo = new int[items.length * 2];
            System.arraycopy(items, 0, novo, 0, n);
            items = novo;
        }
        items[n++] = item;
    }

    public int get(int i) {
        return items[i];
    }

    public Iterator<Integer> iterator() {
        return new IntBagIterator();
    }

    private class IntBagIterator implements Iterator<Integer> {
        private int pos = 0;

        public boolean hasNext() {
            return pos < n;
        }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return items[pos++];
        }
    }
}
