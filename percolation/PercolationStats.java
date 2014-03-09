public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        mean = 0.0;
        stddev = 0.0;
        confidenceLo = 0.0;
        confidenceHi = 0.0;

        double[] x = new double[T];

        for (int t = 0; t < T; ++t) {
            int counter = 0;
            Percolation percolation = new Percolation(N);
            while (true) {
                int index = StdRandom.uniform(N * N);
                int i = index / N + 1;
                int j = index % N + 1;
                while (percolation.isOpen(i, j)) {
                    index = StdRandom.uniform(N * N);
                    i = index / N + 1;
                    j = index % N + 1;
                }
                percolation.open(i, j);
                ++counter;
                if (percolation.percolates()) {
                    break;
                }
            }
            x[t] = (double) counter / (N * N); 
        }

        for (int t = 0; t < T; ++t) {
            mean += x[t]; 
        }
        mean /= T;

        if (T > 1) {
            for (int t = 0; t < T; ++t) {
                stddev += (x[t] - mean) * (x[t] - mean);
            }
            stddev /= T - 1;
        }
        stddev = Math.sqrt(stddev);

        confidenceLo = mean - 1.96 * stddev / Math.sqrt((double) T);
        confidenceHi = mean + 1.96 * stddev / Math.sqrt((double) T);
        
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
