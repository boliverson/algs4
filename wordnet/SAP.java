import java.util.Queue;
import java.util.LinkedList;

public class SAP {
    private Digraph G;
    private Digraph R;

    public SAP(Digraph G) {
        this.G = G;
        this.R = G.reverse();
    }

    public int length(int v, int w) {
        if (!checkBounds(v) || !checkBounds(w)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        Queue<Integer> Q = new LinkedList<Integer>();
        Q.add(v);

        int[] dp = new int[G.V()];
        for (int i = 0; i < G.V(); ++i) {
            dp[i] = Integer.MAX_VALUE;
        }
        dp[v] = 0;

        while (!Q.isEmpty()) {
            int curr = Q.poll();
            if (curr == w) {
                return dp[curr];
            }
            for (int next : G.adj(curr)) {
                if (!checkBounds(next)) {
                    throw new java.lang.IndexOutOfBoundsException();
                }
                if (dp[curr] + 1 < dp[next]) {
                    dp[next] = dp[curr] + 1;
                    Q.add(next);
                }
            }
            for (int next : R.adj(curr)) {
                if (!checkBounds(next)) {
                    throw new java.lang.IndexOutOfBoundsException();
                }
                if (dp[curr] + 1 < dp[next]) {
                    dp[next] = dp[curr] + 1;
                    Q.add(next);
                }
            }
        }

        return -1;
    }

    public int ancestor(int v, int w) {
        if (!checkBounds(v) || !checkBounds(w)) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        Queue<Integer> Q = new LinkedList<Integer>();
        Q.add(v);

        int[] dp = new int[G.V()];
        for (int i = 0; i < G.V(); ++i) {
            dp[i] = Integer.MAX_VALUE;
        }
        dp[v] = 0;

        int[] trace = new int[G.V()];
        for (int i = 0; i < G.V(); ++i) {
            trace[i] = -1;
        }
        trace[v] = v;

        while (!Q.isEmpty()) {
            int curr = Q.peek();
            if (curr == w) {
                break;
            }
            Q.poll();

            for (int next : G.adj(curr)) {
                if (!checkBounds(next)) {
                    throw new java.lang.IndexOutOfBoundsException();
                }
                if (dp[curr] + 1 < dp[next]) {
                    dp[next] = dp[curr] + 1;
                    trace[next] = curr;
                    Q.add(next);
                }
            }

            for (int next : R.adj(curr)) {
                if (!checkBounds(next)) {
                    throw new java.lang.IndexOutOfBoundsException();
                }
                if (dp[curr] + 1 < dp[next]) {
                    dp[next] = dp[curr] + 1;
                    trace[next] = curr;
                    Q.add(next);
                }
            }
        }
        
        if (Q.isEmpty()) {
            return -1;
        }

        for (int curr = w; curr != trace[curr]; curr = trace[curr]) {
            if (checkSuccessor(R, curr, trace[curr])) {
                return curr;
            }
        }

        return -1;
    }

    private boolean checkSuccessor(Digraph R, int v, int w) {
        for (int n : R.adj(v)) {
            if (n == w) {
                return true;
            }
        }
        return false;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int minDistance = Integer.MAX_VALUE;
        for (int nv : v) {
            for (int nw : w) {
                int distance = length(nv, nw);
                if (distance != -1 && distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        if (minDistance == Integer.MAX_VALUE) {
            return -1;
        }
        return minDistance;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int minDistance = Integer.MAX_VALUE;
        int tv = -1;
        int tw = -1;
        for (int nv : v) {
            for (int nw : w) {
                int distance = length(nv, nw);
                if (distance != -1 && distance < minDistance) {
                    minDistance = distance;
                    tv = nv;
                    tw = nw;
                }
            }
        }
        if (minDistance == Integer.MAX_VALUE) {
            return -1;
        }
        return ancestor(tv, tw);
    }

    private boolean checkBounds(int v) {
        return 0 <= v && v < G.V();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
