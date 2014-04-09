public class Outcast {
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        if (nouns == null || nouns.length < 1) {
            return null;
        }
        int maxDistance = distanceToOthers(nouns, 0);
        String outcast = nouns[0];
        for (int v = 1; v < nouns.length; ++v) {
            int distance = distanceToOthers(nouns, v);
            if (maxDistance < distance) {
                maxDistance = distance;
                outcast = nouns[v];
            }
        }
        return outcast;
    }

    private int distanceToOthers(String[] nouns, int v) {
        int distanceSum = 0;
        for (int w = 0; w < nouns.length; ++w) {
            if (w != v) {
                distanceSum += wordnet.distance(nouns[v], nouns[w]);
            }
        }
        return distanceSum;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
