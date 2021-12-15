public class Shell {
    public static void sort(Comparable[] a) {

        int n = a.length;
        int h = 1;
        while (h < n / 3) // Incrementing Sequence - at "which point" of the sequence we will start
            h = 3 * h + 1;

        while (h >= 1) { // While h is still valid (final step is always insertions ort)
            for (int i = h; i < n; i++) { // Cross array linearly
                for (int j = i; j >= h; j -= 1) { // For each element h-positions behind it, make sure the current element is put at its appropriate place.
                    if (!less(a[j], a[j - h])) // Keep going until we make sure elements are in correct order, two by two.
                        break;
                    exch(a, j, j - h);
                }
            }

            h = h / 3; // Progressively decrement until we reach h = 1 (Insertion Sort). This means the insertion sort final pass will be much easier, only having to swap a couple values.
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

        Shell.sort(array);

        for (int i = 0; i < args.length; i++)
            System.out.println(array[i]);
    }
}
