import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private final ArrayList<Board> shiftList;

    public Solver(Board initial) {
        shiftList = new ArrayList<Board>();

        if (initial.isGoal()) {
            shiftList.add(initial);
            return;
        }

        String boardString = initial.toString();
        int end = 0;
        
        while (end < boardString.length()
                && boardString.charAt(end) != '\n') {
            ++end;
        }

        int[] numArray = new int[initial.dimension() * initial.dimension()];
        int numCounter = 0;
        int ind = end;
        while (ind < boardString.length()) {
            while (ind < boardString.length()
                    && !('0' <= boardString.charAt(ind)
                        && boardString.charAt(ind) <= '9')) {
                ++ind;
            }
            if (ind >= boardString.length()) {
                break;
            }
            int j = ind;
            int k = 0;
            while (j < boardString.length()
                    && ('0' <= boardString.charAt(j)
                        && boardString.charAt(j) <= '9')) {
                k *= 10;
                k += boardString.charAt(j) - '0';
                ++j;
            }
            numArray[numCounter++] = k;
            ind = j;
        }

        int zeroRow = 0;
        int inversions = 0;
        for (int i = 0; i < numArray.length; ++i) {
            if (numArray[i] == 0) {
                zeroRow = i / initial.dimension();
            } else {
                for (int j = i + 1; j < numArray.length; ++j) {
                    if (numArray[j] == 0) {
                        continue;
                    }
                    if (numArray[j] < numArray[i]) {
                        ++inversions;
                    }
                }
            }
        }

        if (!((initial.dimension() % 2 != 0 && inversions % 2 == 0)
                    || (initial.dimension() % 2 == 0
                        && zeroRow % 2 != inversions % 2))) {
            return;
        }

        MinPQ<Status> minPQ = new MinPQ<Status>();
        
        Status source = new Status(initial, 0, null);
        for (Status next : nextStatus(source)) {
            minPQ.insert(next);
        }

        Status current = null;

        while (!minPQ.isEmpty()) {
            current = minPQ.delMin();
            if (current.board.isGoal()) {
                break;
            }
            for (Status next : nextStatus(current)) {
                if (next.board.equals(current.previous.board)) {
                    continue;
                }
                minPQ.insert(next);
            }
        }

        while (current != null) {
            shiftList.add(current.board);
            current = current.previous;
        }
        Collections.reverse(shiftList);
    }

    private Iterable<Status> nextStatus(Status current) {
        ArrayList<Status> statusList = new ArrayList<Status>();
        for (Board neighbor : current.board.neighbors()) {
            statusList.add(new Status(neighbor, current.steps + 1, current));
        }
        return statusList;
    }

    private class Status implements Comparable<Status> {
        private final Board board;
        private final int steps;
        private final Status previous;

        private Status(Board board, int steps, Status previous) {
            this.board = board;
            this.steps = steps;
            this.previous = previous;
        }

        public int compareTo(Status that) {
            int h1 = this.board.manhattan() + this.steps;
            int h2 = that.board.manhattan() + that.steps;
            if (h1 < h2) {
                return -1;
            } else if (h1 == h2) {
                return 0;
            }
            return 1;
        }
    }

    public boolean isSolvable() {
        return shiftList.size() != 0;
    }

    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return shiftList.size() - 1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return shiftList;
    }

    public static void main(String[] args) {
        In jin = new In(args[0]);
        int n = jin.readInt();
        int[][] blocks = new int[n][n];

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                blocks[i][j] = jin.readInt();
            }
        }

        Board initial = new Board(blocks);

        Solver solver = new Solver(initial);
        
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
