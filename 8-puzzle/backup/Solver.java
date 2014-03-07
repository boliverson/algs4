import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private final ArrayList<Board> shiftList;

    public Solver(Board initial) {
        int numCounter = 0;
        int[] numArray = new int[initial.dimension() * initial.dimension()];
        String strBoard = initial.toString();
        int t = 0;
        while (strBoard.charAt(t) != '\n') {
            ++t;
        }
        int ind = t;
        while (ind < strBoard.length()) {
            while (ind < strBoard.length()
                    && !('0' <= strBoard.charAt(ind)
                        && strBoard.charAt(ind) <= '9')) {
                ++ind;
            }
            if (ind == strBoard.length()) {
                break;
            }
            int j = ind;
            int k = 0;
            while (j < strBoard.length()
                    && ('0' <= strBoard.charAt(j)
                        && strBoard.charAt(j) <= '9')) {
                k *= 10;
                k += strBoard.charAt(j) - '0';
                ++j;
            }
            numArray[numCounter++] = k;
            ind = j;
        }

        shiftList = new ArrayList<Board>();

        int invCounter = 0;
        for (int i = 1; i < numArray.length; ++i) {
            if (numArray[i] == 0) {
                numArray[i] = numArray.length;
            }
            for (int j = 0; j < i; ++j) {
                if (numArray[i] < numArray[j]) {
                    ++invCounter;
                }
            }
        }

        if (invCounter % 2 == initial.dimension() % 2) {
            return;
        } 

        if (initial.isGoal()) {
            shiftList.add(initial);
            return;
        }

        Status source = new Status(initial, 0, null);
        MinPQ<Status> minPQ = new MinPQ<Status>();

        for (Status next : neighborStatus(source, source.steps)) {
            minPQ.insert(next);
        }

        Status current = null;

        while (!minPQ.isEmpty()) {
            current = minPQ.delMin();
            
            if (current.board.isGoal()) {
                break;
            }

            ArrayList<Status> statusList
                = neighborStatus(current, current.steps);

            for (Status next : statusList) {
                if (next.board.equals(current.previousStatus.board)) {
                    continue;
                }
                minPQ.insert(next);
           }
        }

        while (current != null) {
            shiftList.add(current.board);
            current = current.previousStatus;
        }
        Collections.reverse(shiftList);
    }

    private class Status implements Comparable<Status> {
        private final Board board;
        private final int steps;
        private final Status previousStatus;

        private Status(Board board, int steps, Status previousStatus) {
            this.board = board;
            this.steps = steps;
            this.previousStatus = previousStatus;
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

    private ArrayList<Status> neighborStatus(Status current, int steps) {
        ArrayList<Status> statusList = new ArrayList<Status>();
        for (Board neighborBoard : current.board.neighbors()) {
            statusList.add(new Status(neighborBoard, steps + 1, current));
        }
        return statusList;
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
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
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
