import java.util.Iterator;

public class Queue<Item> implements Iterable<Item> {
    private Node first;
    private Node last;

    private class Node {
        Node next;
        Item data;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void enqueue(Item data) {
        Node node = new Node();
        node.data = data;

        if (!isEmpty())
            last.next = node;
        else
            first = node;

        last = node;
    }

    public Item dequeue() {
        Item data = first.data;
        first = first.next;
        if (isEmpty())
            last = null;
        return data;
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

        public Item next() {
            Item item = current.data;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Queue<String> queue = new Queue<>();
        System.out.println("Queue / Dequeue : ");
        for (String arg : args) {
            if (arg.equals("-"))
                System.out.println(queue.dequeue());
            else
                queue.enqueue(arg);
        }

        System.out.println("Items remaining in list : ");
        for (String s : queue) {
            System.out.println(s);
        }
    }
}
