public class Percolation {
    private int[][] openMap;
    private final int mapSize; // The Width or Length of the percolation map (for a 16 element, it is 4).
    private int openSites;

    // QuickUnion variables
    private int[] id;
    private int[] size;

    // QuickUnion methods
    private void QuickUnion(int arraySize) {
        id = new int[arraySize];
        size = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            id[i] = i;
            size[i] = 1;
        }
    }

    private int root(int p) {
        while (id[p] != p) {
            id[p] = id[id[p]];
            p = id[p];
        }
        return p;
    }

    private boolean connected(int p, int q) {
        return (root(p) == root(q));
    }

    private void union(int p, int q) {
        int i = root(p);
        int j = root(q);

        // If both elements belong to the same root.
        if (i == j)
            return;

        // Get the smaller tree in terms of node count, and attach it to the bigger tree
        if (size[i] < size[j]) {
            id[i] = j;
            size[j] += size[i];
        } else {
            id[j] = i;
            size[i] += size[j];
        }
    }

    // Coordinate to UnionID
    private int getUnionID(int row, int col) {
        return (row - 1) * mapSize + (col - 1);
    }

    private int getUpperID() {
        return mapSize * mapSize;
    }

    private int getLowerID() {
        return getUpperID() + 1;
    }

    // Percolation Methods
    public Percolation(int n) {
        openMap = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                openMap[i][j] = 0;
        mapSize = n;
        openSites = 0;

        // All elements, plus the upper and lower virtual ones.
        QuickUnion(n * n + 2);

        // Connect Upper virtual node with upper ones
        for (int i = 0; i < mapSize; i++)
            union(getUpperID(), i);

        // Connect Lower virtual node with lower ones
        for (int i = mapSize * (mapSize - 1); i < mapSize * mapSize; i++)
            union(getLowerID(), i);
    }

    // Checks if given row and col are valid positions
    private boolean isIllegal(int row, int col) {
        return (row > mapSize || col > mapSize) || (row < 1 || col < 1);
    }


    // X and Y are, respectively, how much to add to row and col
    private void connectAdjacent(int row, int col, int y, int x) {
        // Check if adjacent spot exists
        if (isIllegal(row + y, col + x))
            return; // Just return without throwing error, as means of border treatment (to avoid reaching illegal positions accidentally when looking for adjacent positions.

        // If the adjacent spot is open, then connect it to the center one.
        if (isOpen(row + y, col + x))
            union(getUnionID(row, col), getUnionID(row + y, col + x));
    }

    // Row and Col start at -1
    public void open(int row, int col) {
        // Illegal arguments
        if (isIllegal(row, col))
            throw new IllegalArgumentException();

        // Avoid opening again if it is already open
        if (isOpen(row, col))
            return;

        // Connect to adjacent open squares
        connectAdjacent(row, col, 1, 0);
        connectAdjacent(row, col, -1, 0);
        connectAdjacent(row, col, 0, 1);
        connectAdjacent(row, col, 0, -1);

        openSites++;
        openMap[row - 1][col - 1] = 1;
    }

    /*
    public void testPrint() {
        for (int i = 1; i <= mapSize; i++) {
            for (int j = 1; j <= mapSize; j++) {
                StdOut.print(isOpen(i, j) + " ");
            }
            StdOut.println("");
        }
    }
     */

    public boolean isOpen(int row, int col) {
        // Illegal arguments
        if (isIllegal(row, col))
            throw new IllegalArgumentException();

        return (openMap[row - 1][col - 1] == 1);
    }

    public boolean isFull(int row, int col) {
        // Illegal arguments
        if (isIllegal(row, col))
            throw new IllegalArgumentException();

        // Cannot be full if its not even open
        if (!isOpen(row, col))
            return false;

        // Is connected to the upper entrance "virtual" node
        return connected(getUnionID(row, col), getUpperID());
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        if (mapSize == 1 && !isOpen(1, 1)) // Exception to the rule. Is there a better way to handle this without an additional check here?
            return false;
        return connected(getUpperID(), getLowerID());
    }
}
