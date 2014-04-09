public class SAP {
    private Digraph G;

    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    private boolean checkBounds(int v) {
        return 0 <= v && v < G.V();
    }

    public int length(int v, int w) {
        if (!checkBounds(v) || !checkBounds(w)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);
        int minLength = Integer.MAX_VALUE;
        for (int u = 0; u < G.V(); ++u) {
            if (!bfdpV.hasPathTo(u) || !bfdpW.hasPathTo(u)) {
                continue;
            }
            int dist = bfdpV.distTo(u) + bfdpW.distTo(u);
            if (dist < minLength) {
                minLength = dist;
            }
        }
        if (minLength == Integer.MAX_VALUE) {
            return -1;
        } else {
            return minLength;
        }
    }

    public int ancestor(int v, int w) {
        if (!checkBounds(v) || !checkBounds(w)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);
        int minLength = Integer.MAX_VALUE;
        int anc = -1;
        for (int u = 0; u < G.V(); ++u) {
            if (!bfdpV.hasPathTo(u) || !bfdpW.hasPathTo(u)) {
                continue;
            }
            int dist = bfdpV.distTo(u) + bfdpW.distTo(u);
            if (dist < minLength) {
                minLength = dist;
                anc = u;
            }
        }
        return anc;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (int iv : v) {
            if (!checkBounds(iv)) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        for (int iw : w) {
            if (!checkBounds(iw)) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        int minLength = Integer.MAX_VALUE;
        for (int iv : v) {
            for (int iw : w) {
                int dist = length(iv, iw);
                if (dist == -1) {
                    continue;
                }
                if (dist < minLength) {
                    minLength = dist;
                }
            }
        }
        if (minLength == Integer.MAX_VALUE) {
            return -1;
        } else {
            return minLength;
        }
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (int iv : v) {
            if (!checkBounds(iv)) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        for (int iw : w) {
            if (!checkBounds(iw)) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        int minLength = Integer.MAX_VALUE;
        int anc = -1;
        for (int iv : v) {
            for (int iw : w) {
                int dist = length(iv, iw);
                if (dist == -1) {
                    continue;
                }
                if (dist < minLength) {
                    minLength = dist;
                    anc = ancestor(iv, iw);
                }
            }
        }
        return anc;
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
