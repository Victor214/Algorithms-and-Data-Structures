public class Insertion {
    public static void sort(Comparable[] a) {
        int n = a.length;
        for (int i = 1; i < n; i++)
            for (int j = i; j > 0; j--) {
                if (!less(a[j], a[j - 1])) // If the current element is greater than the previous one, then its already ordered.
                    break;
                exch(a, j, j - 1);
            }
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args) {
        String[] array = new String[args.length];
        for (int i = 0; i < args.length; i++)
            array[i] = args[i];

        Insertion.sort(array);

        for (int i = 0; i < args.length; i++)
            System.out.println(array[i]);
    }
}
