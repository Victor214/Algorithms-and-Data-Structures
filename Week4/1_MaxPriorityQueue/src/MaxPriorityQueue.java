public class MaxPriorityQueue<Key extends Comparable<Key>> {
    private Key[] priorityqueue;
    private int n;

    public MaxPriorityQueue(int capacity) {
        priorityqueue = (Key[]) new Comparable[capacity + 1];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public void insert(Key key) {
        priorityqueue[++n] = key;
        swim(n);
    }

    public Key deleteMax() {
        Key result = priorityqueue[1];
        exch(1, n--);
        sink(1);
        priorityqueue[n + 1] = null;
        return result;
    }

    private void swim(int k) {
        while (k > 1 && less(k / 2, k)) { // While we are not at the root, and parent is less than its children
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && less(j, j + 1)) // If left child is less than right child, then set j as the right child. Otherwise, remain equal. Effectively, this sets j as the greater child of the two.
                j++;
            if (!less(k, j)) // If the parent is greater or equal than the greater child of the two, then it means it is already in correct order.
                break;

            exch(k, j);
            k = j;
        }
    }

    private boolean less(int i, int j) {
        return priorityqueue[i].compareTo(priorityqueue[j]) < 0;
    }

    private void exch(int i, int j) {
        Key t = priorityqueue[i];
        priorityqueue[i] = priorityqueue[j];
        priorityqueue[j] = t;
    }

    public static void main(String[] args) {
        MaxPriorityQueue<Integer> pq = new MaxPriorityQueue<>(10);
        pq.insert(5);
        pq.insert(3);
        pq.insert(8);
        pq.insert(2);
        pq.insert(9);
        pq.insert(1);
        pq.insert(7);
        System.out.println("Max : " + pq.deleteMax());
        System.out.println("Max : " + pq.deleteMax());
        System.out.println("Max : " + pq.deleteMax());
    }
}
