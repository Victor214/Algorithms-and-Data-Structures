import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private boolean solvable;
    private int totalmoves;
    private List<Board> solvesequence;

    // Create inner class SearchNode
    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previous;
        private final int priority;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.priority = board.manhattan() + moves;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        MinPQ<SearchNode> solution = new MinPQ<>();
        solution.insert(new SearchNode(initial, 0, null));

        MinPQ<SearchNode> twinSolution = new MinPQ<>();
        twinSolution.insert(new SearchNode(initial.twin(), 0, null));

        List<Board> sequenceList = new ArrayList<>(); // Solution list, if there is a solution

        while (true) { // Is this the right condition?
            SearchNode node = solution.delMin();
            SearchNode twinNode = twinSolution.delMin();
            sequenceList.add(node.getBoard());

            // Stop conditions (either of the boards must be solvable)
            if (node.getBoard().isGoal()) {
                solvable = true;
                totalmoves = node.getMoves();
                solvesequence = sequenceList;
                return;
            }
            if (twinNode.getBoard().isGoal()) {
                solvable = false;
                totalmoves = -1;
                solvesequence = null;
                return;
            }

            // Next step
            addNeighbors(solution, node);
            addNeighbors(twinSolution, twinNode);
        }
    }

    // Do next-step in getting neighbors and adding them onto the solution
    private void addNeighbors(MinPQ<SearchNode> solution, SearchNode node) {
        SearchNode previousNode = node.getPrevious();
        Iterable<Board> neighbors = node.getBoard().neighbors();
        int addedCount = 0;
        for (Board neighbor : neighbors) {
            if (previousNode != null && neighbor.equals(previousNode.getBoard())) // Critical Optimization (don't add repeating boards
                continue;
            addedCount++;
            SearchNode neighborNode = new SearchNode(neighbor, node.getMoves() + 1, node);
            solution.insert(neighborNode);
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return totalmoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solvesequence;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
