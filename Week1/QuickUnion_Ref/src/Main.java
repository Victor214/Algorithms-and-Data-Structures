import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// Compile : javac-algs4 Main.java QuickUnion.java
// Run : java-algs4 Main < input.txt

public class Main {
    public static void main(String[] args) {
        int n = StdIn.readInt();
        QuickUnion uf = new QuickUnion(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (!uf.connected(p, q)) {
                uf.union(p, q);
                StdOut.println(p + " " + q);
            }
        }
    }
}
