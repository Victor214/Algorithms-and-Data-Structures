import java.util.NoSuchElementException;

public class Queue {
    private String[] q;
    private int head, tail;
    private int capacity;
    int n;

    public Queue(int capacity) {
        this.capacity = capacity;
        q = new String[capacity];
        head = tail = n = 0;
    }

    private int addOne(int number) {
        return (number + 1) % capacity;
    }

    public void enqueue(String data) {
        if (n >= capacity)
            throw new RuntimeException("Queue cannot be enqueued while full.");

        q[tail] = data;
        tail = addOne(tail);
        n++;
    }

    public String dequeue() {
        if (n == 0)
            throw new NoSuchElementException("Queue cannot be dequeued while empty.");

        String item = q[head];
        head = addOne(head);
        n--;
        return item;
    }

    public static void main(String[] args) {
        Queue queue = new Queue(10);
        for (String arg : args) {
            if (arg.equals("-"))
                System.out.println(queue.dequeue());
            else
                queue.enqueue(arg);
        }
    }
}
