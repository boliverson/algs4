public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final int width;
    private final int source;
    private final int sink;
    private final boolean[] marked;

    public Percolation(int N) {
        uf = new WeightedQuickUnionUF(N * N + 2);
        width = N;
        source = 0;
        sink = N * N + 1;
        marked = new boolean[N * N + 2];
    }

    public void open(int i, int j) {
        if (!checkBounds(i, j)) {
            throw new IndexOutOfBoundsException();
        }
        int index = getIndex(i, j);
        if (marked[index]) {
            return;
        }
        int[][] dirs = new int[][] {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        marked[index] = true;
        if (i == 1) {
            uf.union(index, source);
        }
        if (i == width) {
            uf.union(index, sink);
        }
        for (int k = 0; k < 4; ++k) {
            int u = i + dirs[k][0];
            int v = j + dirs[k][1];
            if (!checkBounds(u, v)) {
                continue;
            }
            if (isOpen(u, v)) {
                uf.union(index, getIndex(u, v));
            }
        }
    }
    
    public boolean isOpen(int i, int j) {
        if (!checkBounds(i, j)) {
            throw new IndexOutOfBoundsException();
        }
        return marked[getIndex(i, j)];
    }
    
    public boolean isFull(int i, int j) {
        if (!checkBounds(i, j)) {
            throw new IndexOutOfBoundsException();
        }
        return uf.connected(source, getIndex(i, j));
    }

    public boolean percolates() {
        return uf.connected(source, sink);
    }

    private boolean checkBounds(int i, int j) {
        return 1 <= i && i <= width && 1 <= j && j <= width;
    }

    private int getIndex(int i, int j) {
        return (i - 1) * width + j;
    }
}
