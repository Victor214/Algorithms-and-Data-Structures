import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length; // length on a 2D array returns the number of rows.
        this.tiles = new int[n][];
        for (int i = 0; i < tiles.length; i++)
            this.tiles[i] = tiles[i].clone();
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.append(tiles[i][j]).append(" ");
            }
            result.deleteCharAt(result.length() - 1); // Delete last space
            result.append("\n");
        }
        result.deleteCharAt(result.length() - 1); // Delete last break line
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                if (val == 0)
                    continue;
                if (val != (i * n + (j + 1)))
                    result++;
            }
        }
        return result;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                if (val == 0)
                    continue;

                int currentRow = i + 1;
                int currentCol = j + 1;
                int targetRow = (val - 1) / n + 1;
                int targetCol = (val - 1) % n + 1;

                result += Math.abs(targetRow - currentRow) + Math.abs(targetCol - currentCol); // How far the value in current tile is from its correct (target) position.
            }
        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                if (val == 0)
                    continue;
                if (val != (i * n + (j + 1))) // One different value than desired is enough.
                    return false;
            }
        }
        return true; // All values match their appropriate positions.
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;

        if (!(y.getClass().getSimpleName().equals("Board")))
            return false;

        final Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;

        return true;
    }

    private void swap(int[][] array, int row1, int col1, int row2, int col2) {
        int aux = array[row1][col1];
        array[row1][col1] = array[row2][col2];
        array[row2][col2] = aux;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> result = new ArrayList<>();

        // First, loop thru array and find 0.
        int zeroRow = 0, zeroCol = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (this.tiles[i][j] == 0) {
                    zeroRow = i + 1;
                    zeroCol = j + 1;
                    break;
                }

        if (zeroRow == 0) // Value of 0 was not found in array.
            return null;

        // Following loop creates 4 combinations : i:-1 / j:0 ~~ i:1 / j:0 ~~ i:0 / j:1 ~~ i:0 / j:-1
        // Each pair is gonna be added onto zero's coordinates, checked if the resulting coordinates are valid, and then swapped positions with zero, creating a neighbour board.
        Integer[] iAxis = {-1, 1, 0, 0};
        Integer[] jAxis = {0, 0, 1, -1};

        for (int k = 0; k < iAxis.length; k++) {
            int nbRow = zeroRow + iAxis[k]; // Neighbour row and col to be exchanged with zero.
            int nbCol = zeroCol + jAxis[k];

            if ((nbRow > n) || (nbRow < 1)) // Boundary treatment for i
                continue;

            if ((nbCol > n) || (nbCol < 1)) // Boundary treatment for j
                continue;

            // Remaining arrays are all valid, just swap 0 with iAxis / jAxis and add it to result
            int[][] neighborTiles = new int[n][];
            for (int i = 0; i < n; i++)
                neighborTiles[i] = tiles[i].clone();

            // Swap tiles (zero tile, and current targeting neighbor tile)
            // Rows and Cols here are 1-indexed, so we need to make them 0-based for array indexing.
            swap(neighborTiles, zeroRow - 1, zeroCol - 1, nbRow - 1, nbCol - 1);

            Board neighborBoard = new Board(neighborTiles);
            result.add(neighborBoard);
        }

        return result;
    }

    // Gets the first two, non null tiles for the twin board
    private int[][] getFirstTiles() {
        int i1 = -1, j1 = -1;
        int i2 = -1, j2 = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) // We don't want an element if it equals zero (empty).
                    continue;

                if (i1 == -1) { // If 1st element isn't picked yet, then select it, and continue onto next iteration.
                    i1 = i;
                    j1 = j;
                    continue;
                }

                // Only reachable if 1st element is already picked, therefore pick second and exit.
                i2 = i;
                j2 = j;
                return new int[][]{{i1, j1}, {i2, j2}};
            }
        }

        return new int[][]{{-1, -1}, {-1, -1}}; // Should never be reached for valid arrays
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // Find the first two non-zero elements in array, swap them, and return the given board.
        int[][] exchTiles = getFirstTiles();

        // Create twin tiles array as a copy of the original one
        int[][] twinTiles = new int[n][];
        for (int i = 0; i < n; i++)
            twinTiles[i] = tiles[i].clone();

        // Swap tiles (the first two non-zero tiles)
        // No need to decrement 1 from the following indexes, considering they are already 0-based.
        swap(twinTiles, exchTiles[0][0], exchTiles[0][1], exchTiles[1][0], exchTiles[1][1]); // Swap i1, j1 with i2, j2
        return new Board(twinTiles);
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        // Do testing
        int[][] tiles = {
                {8, 0, 3},
                {4, 1, 2},
                {7, 6, 5}
        };
        Board board = new Board(tiles);

        System.out.println(board.toString()); // Printing
        System.out.println("Dimension : " + board.dimension());
        System.out.println("Hamming : " + board.hamming());
        System.out.println("Manhattan : " + board.manhattan());
        System.out.println("isGoal : " + board.isGoal());

        System.out.println("-------------------");
        for (Board neighbour : board.neighbors()) {
            System.out.println(neighbour.toString());
            System.out.println("-----");
        }
        System.out.println("-------------------");
        System.out.println("Twin :");
        System.out.println(board.twin().toString());

        int[][] tiles2 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        Board board2 = new Board(tiles2);
        System.out.println("Board1 equals Board2 : " + board.equals(board2));
        System.out.println("Board2 isGoal : " + board2.isGoal());


    }

}
