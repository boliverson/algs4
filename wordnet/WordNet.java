import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class WordNet {
    private ArrayList<String> synsetList;
    private Map<String, ArrayList<Integer>> synsetMap;
    private Digraph G;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        synsetList = new ArrayList<String>();
        synsetMap = new HashMap<String, ArrayList<Integer>>();

        In synsetsIn = new In(synsets);
        while (synsetsIn.hasNextLine()) {
            String strLine = synsetsIn.readLine();
            String[] tokens = strLine.split(",");
            int id = Integer.parseInt(tokens[0]);
            if (synsetList.size() < id) {
                synsetList.ensureCapacity(id + 1);
            }
            synsetList.add(tokens[1]);
            String[] nouns = tokens[1].split(" ");
            for (String noun : nouns) {
                if (!synsetMap.containsKey(noun)) {
                    synsetMap.put(noun, new ArrayList<Integer>());
                }
                synsetMap.get(noun).add(id);
            }
        }
        synsetsIn.close();

        G = new Digraph(synsetList.size());
        int[] outputs = new int[synsetList.size()];

        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String strLine = hypernymsIn.readLine();
            String[] tokens = strLine.split(",");
            int v = Integer.parseInt(tokens[0]);
            outputs[v] += tokens.length - 1;

            for (int i = 1; i < tokens.length; ++i) {
                int w = Integer.parseInt(tokens[i]);
                G.addEdge(v, w);
            }
        }
        hypernymsIn.close();

        if (!checkRootedDAG(outputs)) {
            throw new IllegalArgumentException();
        }

        sap = new SAP(G);
    }

    private boolean checkRootedDAG(int[] outputs) {
        int rootCount = 0;
        for (int v = 0; v < G.V(); ++v) {
            if (outputs[v] == 0) {
                ++rootCount;
            }
        }
        if (rootCount != 1) {
            return false;
        }
        if (new KosarajuSharirSCC(G).count() < synsetList.size()) {
            return false;
        }
        return true;
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
        return sap.length(synsetMap.get(nounA), synsetMap.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }
        return synsetList.get(sap.ancestor(synsetMap.get(nounA),
                    synsetMap.get(nounB)));
    }

    public static void main(String[] args) {
    }
}
