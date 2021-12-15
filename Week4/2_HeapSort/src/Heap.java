public class Heap {
    public static void sort(Comparable[] pq) {
        int n = pq.length;
        for (int k = n / 2; k >= 1; k--) // Start from half of the array in reverse order, which means, we traverse the array bottom-up, ignoring the leafs and starting at the inner ones, doing the sink operation, transforming it into a heap structure.
            sink(pq, k, n);

        while (n > 1) { // Remove the first element (max), put it on the end of the array, and adjust heap to keep its properties.
            exch(pq, 1, n);
            sink(pq, 1, --n);
        }
    }

    private static void sink(Comparable[] pq, int k, int n) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && less(pq, j, j + 1)) // If left child is less than right child, then set j as the right child. Otherwise, remain equal. Effectively, this sets j as the greater child of the two.
                j++;
            if (!less(pq, k, j)) // If the parent is greater or equal than the greater child of the two, then it means it is already in correct order.
                break;

            exch(pq, k, j);
            k = j;
        }
    }

    private static boolean less(Comparable[] pq, int i, int j) {
        i--;
        j--;
        return pq[i].compareTo(pq[j]) < 0;
    }

    private static void exch(Comparable[] pq, int i, int j) {
        i--;
        j--;
        Comparable t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
    }

    public static void main(String[] args) {
        Integer[] testArray = new Integer[]{7, 9, 2, 4, 85, 12, 43, 8, 75, 14, 17, 19, 33, 84};
        Heap.sort(testArray);
        for (Integer i : testArray)
            System.out.print(i + " ");
        System.out.println();
    }
}
