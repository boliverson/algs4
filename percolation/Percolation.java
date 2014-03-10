public class Percolation {
    private WeightedQuickUnionUF topUF;
    private WeightedQuickUnionUF bottomUF;
    private int width;  
    private boolean[] marked;
    private int top = 0;
    private int bottom;
    private boolean percolates = false;

    public Percolation(int N) {
        topUF = new WeightedQuickUnionUF(N * N + 2);
        bottomUF = new WeightedQuickUnionUF(N * N + 2);
        width = N;
        marked = new boolean[N * N + 2];
        bottom = N * N + 1;
    }

    public void open(int i, int j) {
        if (!checkBounds(i, j)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int current = getIndex(i, j);
        if (marked[current]) {
            return;
        }
        marked[current] = true;
        if (i == 1) {
            topUF.union(top, current);
        }
        if (i == width) {
            bottomUF.union(current, bottom);
        }
        final int[][] dirs = new int[][] { {0, -1}, {-1, 0}, {0, 1}, {1, 0} };
        for (int k = 0; k < 4; ++k) {
            int u = i + dirs[k][0];
            int v = j + dirs[k][1];
            if (!checkBounds(u, v)) {
                continue;
            }
            int neighbor = getIndex(u, v);
            if (isOpen(u, v)) {
                topUF.union(current, neighbor);
                bottomUF.union(current, neighbor);
                if (checkPivot(u, v)) {
                    percolates = true;
                }
            }
        }
        if (checkPivot(i, j)) {
            percolates = true;
        }
    }

    public boolean isOpen(int i, int j) {
        if (!checkBounds(i, j)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return marked[getIndex(i, j)];
    }

    public boolean isFull(int i, int j) {
        if (!checkBounds(i, j)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return topUF.connected(top, getIndex(i, j));
    }

    public boolean percolates() {
        return percolates;
    }

    private int getIndex(int i, int j) {
        return (i - 1) * width + j;
    }

    private boolean checkBounds(int i, int j) {
        return 1 <= i && i <= width && 1 <= j && j <= width;
    }

    private boolean checkPivot(int i, int j) {
        int index = getIndex(i, j);
        return topUF.connected(top, index)
            && bottomUF.connected(index, bottom);
    }
}
