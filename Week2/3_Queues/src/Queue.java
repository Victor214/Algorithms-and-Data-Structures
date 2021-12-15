public class Queue {
    private Node first;
    private Node last;

    private class Node {
        Node next;
        String data;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void enqueue(String data) {
        Node node = new Node();
        node.data = data;

        if (!isEmpty())
            last.next = node;
        else
            first = node;

        last = node;
    }

    public String dequeue() {
        String data = first.data;
        first = first.next;
        if (isEmpty())
            last = null;
        return data;
    }

    public static void main(String[] args) {
        Queue queue = new Queue();
        for (String arg : args) {
            if (arg.equals("-"))
                System.out.println(queue.dequeue());
            else
                queue.enqueue(arg);
        }
    }

}
