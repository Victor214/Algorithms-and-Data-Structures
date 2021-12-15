import edu.princeton.cs.algs4.StdRandom;

public class QuickSort {

    private static int partition(Comparable[] a, int lo, int hi) {

        int i = lo, j = hi + 1;
        while (true) {
            while (less(a[++i], a[lo])) // Makes sure i is where it is supposed to be (first element that is greater than lo for the swap), from left to right.
                if (i == hi) break; // If we reach the end of array, break.

            while (less(a[lo], a[--j])) // Makes sure j is where it is supposed to be (first element that is less than lo for the swap), from right to left.
                if (j == lo) break; // If we reach the end of array, break.

            if (i >= j) // If pointers cross, also break.
                break;

            // i and j are ready to be swapped, and we haven't reached any ending conditions.
            exch(a, i, j);
        }

        exch(a, lo, j); // Finally, swap the first element (lo) with j (its correct position to be the "middle" element)
        return j;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) // Recursion stop condition
            return;

        int j = partition(a, lo, hi);
        sort(a, lo, j - 1);
        sort(a, j + 1, hi);
    }

    public static void main(String[] args) {
        String[] array = new String[args.length];
        for (int i = 0; i < args.length; i++)
            array[i] = args[i];

        QuickSort.sort(array);

        for (int i = 0; i < args.length; i++)
            System.out.println(array[i]);
    }
}
