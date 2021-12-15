public class Queue<Item> {
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

    public static void main(String[] args) {
        Queue<String> queue = new Queue<>();
        for (String arg : args) {
            if (arg.equals("-"))
                System.out.println(queue.dequeue());
            else
                queue.enqueue(arg);
        }
    }

}
