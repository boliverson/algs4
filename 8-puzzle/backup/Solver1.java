import java.util.ArrayList;

public class Solver {
    private final ArrayList<Board> shiftList;

    public Solver(Board initial) {
        shiftList = new ArrayList<Board>();
        if (initial.dimension() == 0) {
            return;
        }
        if (initial.isGoal()) {
            shiftList.add(initial);
            return;
        }
        int t = 0;
        String boardStr = initial.toString();
        while (boardStr.charAt(t) != '\n') {
            ++t;
        }
        int[] numArray = new int[initial.dimension() * initial.dimension()];
        int ind = 0;
        while (ind < boardStr.length()) {
            while (ind < boardStr.length()
                    && !('0' <= boardStr.charAt(ind)
                        && boardStr.charAt(ind) <= '9')) {
                ++ind;
            }
            int j = ind;
            int k = 0;
            while (j < boardStr.length()
                    && ('0' <= boardStr.charAt(j)
                        && boardStr.charAt(j) <= '9')) {
                k *= 10;
                k += boardStr.charAt(j) - '0';
                ++j;
            }
            ind = j;
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

    private final class Status {
        private final Board board;
        private final int steps;
        private final Status previous;

        private Status(Board board, int steps, Status previous) {
            this.board = board;
            this.steps = steps;
            this.previous = previous;
        }
    }

    public static void main(String[] args) {
    }
}
