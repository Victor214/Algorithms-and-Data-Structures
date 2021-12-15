import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        double[] result = new double[trials];
        mean = 0;
        stddev = 0;
        confidenceLo = 0;
        confidenceHi = 0;

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                // An optimization that could be done here is, instead of creating random numbers on demand, initiate an array at the start ranging from 1 to n, shuffle this array, and pick numbers in order. That way, you won't have repetition.
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
            }
            result[i] = (double) percolation.numberOfOpenSites() / (n * n);
            mean += result[i];
        }

        // Calculates mean
        mean = mean / trials;

        // Calculates stddev
        for (int i = 0; i < trials; i++) {
            stddev += Math.pow(result[i] - mean, 2);
        }
        stddev = stddev / (trials - 1);
        stddev = Math.sqrt(stddev);

        // Calculates low and high confidence interval
        confidenceLo = mean - (1.96 * stddev) / Math.sqrt(trials);
        confidenceHi = mean + (1.96 * stddev) / Math.sqrt(trials);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }

        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, T);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
