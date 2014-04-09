import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class WordNet {

    private class DepthFirstOrder implements Iterable<Integer> {
        private ArrayList<Integer> order;
        private boolean[] marked;

        public DepthFirstOrder(Digraph G) {
            order = new ArrayList<Integer>();
            marked = new boolean[G.V()];
            for (int v = 0; v < G.V(); ++v) {
                if (!marked[v]) {
                    dfs(G, v);
                }
            }
            Collections.reverse(order);
        }

        private void dfs(Digraph G, int v) {
            marked[v] = true;
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    dfs(G, w);
                }
            }
            order.add(v);
        }

        @Override
        public Iterator<Integer> iterator() {
            return order.iterator();
        }
    }

    private class KosarajuSharirSCC {
        private boolean[] marked;
        private int[] id;
        private int count;

        public KosarajuSharirSCC(Digraph G) {
            marked = new boolean[G.V()];
            id = new int[G.V()];
            DepthFirstOrder reverseOrder = new DepthFirstOrder(G.reverse());

            for (int v : reverseOrder) {
                if (!marked[v]) {
                    dfs(G, v);
                    ++count;
                }
            }
        }

        private void dfs(Digraph G, int v) {
            marked[v] = true;
            id[v] = count;
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    dfs(G, w);
                }
            }
        }

        public int count() {
            return count;
        }

    }

    private ArrayList<String> synsetList;
    private Map<String, ArrayList<Integer>> synsetMap;
    private Digraph G;
    private SAP sap;

    private int[] inCounts;
    private int[] outCounts;

    public WordNet(String synsets, String hypernyms) {
        synsetList = new ArrayList<String>();
        synsetMap = new HashMap<String, ArrayList<Integer>>();

        In in = new In(synsets);

        while (!in.isEmpty()) {
            String[] tokens = in.readLine().split(",");
            int id = Integer.parseInt(tokens[0]);
            String[] nouns = tokens[1].split(" ");
            for (int i = 0; i < nouns.length; ++i) {
                if (synsetMap.get(nouns[i]) == null) {
                    synsetMap.put(nouns[i], new ArrayList<Integer>());
                }
                synsetMap.get(nouns[i]).add(id);
            }
            synsetList.add(tokens[1]);
        }
        in.close();

        G = new Digraph(synsetList.size());

        inCounts = new int[G.V()];
        outCounts = new int[G.V()];

        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] tokens = in.readLine().split(",");
            int v = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; ++i) {
                int w = Integer.parseInt(tokens[i]);
                G.addEdge(v, w);

                ++inCounts[w];
                ++outCounts[v];
            }
        }
        in.close();

        if (!detectDirectedAcyclicGraph()) {
            throw new java.lang.IllegalArgumentException();
        }


        sap = new SAP(G);
    }

    private boolean detectDirectedAcyclicGraph() {
        int n = G.V();
        int rootCount = 0;
        for (int v = 0; v < n; ++v) {
            if (outCounts[v] == 0) {
                ++rootCount;
            }
        }
        if (rootCount != 1) {
            return false;
        }
        return new KosarajuSharirSCC(G).count() == n;
    }

    public Iterable<String> nouns() {
        return synsetMap.keySet();
    }

    public boolean isNoun(String word) {
        return synsetMap.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }
        ArrayList<Integer> la = synsetMap.get(nounA);
        ArrayList<Integer> lb = synsetMap.get(nounB);
        return sap.length(la, lb); 
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }
        ArrayList<Integer> lv = synsetMap.get(nounA);
        ArrayList<Integer> lw = synsetMap.get(nounB);
        return synsetList.get(sap.ancestor(lv, lw));
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readLine();
            String nounB = StdIn.readLine();
            StdOut.println(wordnet.distance(nounA, nounB));
        }
    }

}
