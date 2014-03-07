import java.util.ArrayList;

public class Board {
    private final int[][] blocks;

    public Board(int[][] blocks) {
        this.blocks = new int[blocks.length][blocks.length];
        for (int r = 0; r < dimension(); ++r) {
            for (int c = 0; c < dimension(); ++c) {
                this.blocks[r][c] = blocks[r][c];
            }
        }
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        int dist = 0;
        for (int r = 0; r < dimension(); ++r) {
            for (int c = 0; c < dimension(); ++c) {
                if (blocks[r][c] == 0) {
                    continue;
                }
                if (blocks[r][c] - 1 != r * dimension() + c) {
                    ++dist;
                }
            }
        }
        return dist;
    }

    public int manhattan() {
        int dist = 0;
        for (int r = 0; r < dimension(); ++r) {
            for (int c = 0; c < dimension(); ++c) {
                if (blocks[r][c] == 0) {
                    continue;
                }
                dist += Math.abs(r - (blocks[r][c] - 1) / dimension())
                    + Math.abs(c - (blocks[r][c] - 1) % dimension());
            }
        }
        return dist;
    }

    public boolean isGoal() {
        for (int r = 0; r < dimension(); ++r) {
            for (int c = 0; c < dimension(); ++c) {
                if (blocks[r][c] == 0) {
                    continue;
                }
                if (blocks[r][c] - 1 != r * dimension() + c) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board twin() {
        Board twinBoard = new Board(this.blocks);
        if (dimension() < 2) {
            return twinBoard;
        }
        int temp;
        if (twinBoard.blocks[0][0] != 0 && twinBoard.blocks[0][1] != 0) {
            temp = twinBoard.blocks[0][0];
            twinBoard.blocks[0][0] = twinBoard.blocks[0][1];
            twinBoard.blocks[0][1] = temp;
        } else {
            temp = twinBoard.blocks[1][0];
            twinBoard.blocks[1][0] = twinBoard.blocks[1][1];
            twinBoard.blocks[1][1] = temp;
        }
        return twinBoard;
    }

    public boolean equals(Object y) {
        if (y instanceof Board) {
            Board that = (Board) y;
            if (this.dimension() != that.dimension()) {
                return false;
            }
            for (int r = 0; r < dimension(); ++r) {
                for (int c = 0; c < dimension(); ++c) {
                    if (this.blocks[r][c] != that.blocks[r][c]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        int[][] directions = new int[][] { {-1, 0}, {0, -1}, {1, 0}, {0, 1} };
        int cr = 0;
        int cc = 0;
        for (int r = 0; r < dimension(); ++r) {
            for (int c = 0; c < dimension(); ++c) {
                if (blocks[r][c] == 0) {
                    cr = r;
                    cc = c;
                }
            }
        }
        for (int ind = 0; ind < 4; ++ind) {
            int nr = cr + directions[ind][0];
            int nc = cc + directions[ind][1];
            if (0 <= nr && nr < dimension() && 0 <= nc && nc < dimension()) {
                Board neighbor = new Board(this.blocks);
                int temp = neighbor.blocks[nr][nc];
                neighbor.blocks[nr][nc] = neighbor.blocks[cr][cc];
                neighbor.blocks[cr][cc] = temp;
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(Integer.toString(dimension()) + "\n");
        for (int r = 0; r < dimension(); ++r) {
            for (int c = 0; c < dimension(); ++c) {
                str.append(" " + blocks[r][c]);
            }
            str.append("\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }

        Board initial = new Board(blocks);
        StdOut.println("Initial: \n" + initial + "Neighbors: \n");

        for (Board neighbor : initial.neighbors()) {
            StdOut.println(neighbor);
        }

        StdOut.println(initial.twin());
    }
}
