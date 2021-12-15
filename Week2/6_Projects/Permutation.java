import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Permutation {
    private RandomizedQueue<String> rq;
    private final int k;

    private Permutation(int k) {
        rq = new RandomizedQueue<>();
        this.k = k;
    }

    private void add(String value) {
        rq.enqueue(value);
    }

    private void printall() {
        for (int i = 0; i < k; i++) {
            if (rq.isEmpty())
                break;
            String item = rq.dequeue();
            StdOut.println(item);
        }
    }

    public static void main(String[] args) {
        int k = StdIn.readInt();

        Permutation permutation = new Permutation(k);
        while (!StdIn.isEmpty()) {
            String value = StdIn.readString();
            permutation.add(value);
        }
        permutation.printall();
    }
}
