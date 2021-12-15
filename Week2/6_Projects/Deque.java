import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int count;

    private class Node {
        Node prev;
        Node next;
        Item data;
    }

    // Construct an empty queue
    public Deque() {
        first = null;
        last = null;
        count = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node newNode = new Node();
        newNode.data = item;
        if (!isEmpty()) {
            newNode.next = first;
            first.prev = newNode;
        } else
            last = newNode; // When queue is empty, we need to make sure both first and last point at the new node.

        first = newNode;
        count++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node newNode = new Node();
        newNode.data = item;
        if (!isEmpty()) {
            last.next = newNode;
            newNode.prev = last;
        } else
            first = newNode;

        last = newNode;
        count++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();

        Node node = first;
        first = first.next;
        if (isEmpty()) // If it was a single element queue, then its empty now.
            last = null;
        else // Else, it means the queue has at least one element.
            first.prev = null;
        count--;
        return node.data;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();

        Node node = last;
        last = last.prev;
        if (last == null) // If it was a single element queue, then its empty now.
            first = null;
        else // Else, it means the queue has at least one element.
            last.next = null;
        count--;
        return node.data;
    }

    // Iterator Implementation
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = current.data;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();

        System.out.println("isEmpty : " + deque.isEmpty());

        deque.addFirst("1");
        deque.addFirst("2");
        deque.addFirst("3");

        System.out.println("isEmpty : " + deque.isEmpty());

        String s = deque.removeLast();
        System.out.println("Removed : " + s);
        deque.addLast("4");

        // First set of outputs
        for (String current : deque) {
            System.out.println(current);
        }
        System.out.println("Size (1) : " + deque.size());

        deque.addLast("5");
        deque.addLast("6");
        deque.addLast("7");
        s = deque.removeFirst();
        System.out.println("Removed : " + s);

        // Second set of outputs
        for (String current : deque) {
            System.out.println(current);
        }
        System.out.println("Size (2) : " + deque.size());
    }

}
