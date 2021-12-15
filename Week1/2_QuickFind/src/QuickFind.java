public class QuickFind {
    private int[] id;

    public QuickFind(int n) {
        id = new int[n];
        for (int i = 0; i < n; i++)
            id[i] = i;
    }

    public void union(int p, int q) {
        int copyId = id[q]; // We want to copy the value of Q into all values of P
        int receiveId = id[p];
        for (int i = 0; i < id.length; i++) {
            if (id[i] == receiveId)
                id[i] = copyId;
        }
    }

    public boolean connected(int p, int q) {
        return (id[p] == id[q]);
    }

}
