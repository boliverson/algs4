public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        mean = 0.0;
        stddev = 0.0;
        confidenceLo = 0.0;
        confidenceHi = 0.0;

        int[] records = new int[T];

        for (int t = 0; t < T; ++t) {
            int counter = 0;
            Percolation percolation = new Percolation(N);
            for (int k = 1; true; ++k) {
                int index = StdRandom.uniform(N * N);
                int i = index / N + 1;
                int j = index % N + 1;
                while (percolation.isOpen(i, j)) {
                    index = StdRandom.uniform(N * N);
                    i = index / N + 1;
                    j = index % N + 1;
                }
                percolation.open(i, j);
                if (percolation.percolates()) {
                    counter += k;
                    break;
                }
            }
            records[t] = counter;
        }

        for (int t = 0; t < T; ++t) {
        }
        
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
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(N, T);
        StdOut.println("mean                    = "
                + percolationStats.mean());
        StdOut.println("stddev                  = "
                + percolationStats.stddev());
        StdOut.println("95% confidence interval = "
                + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi());
    }
}
