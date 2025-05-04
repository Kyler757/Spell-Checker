package src;
import src.model.DAWG;
import src.model.FindWords;
import src.model.Print;
import src.model.Load;

import java.util.ArrayList;
import java.util.BitSet;

public class App {
    public static void main(String[] args) throws Exception {
        Print.clearScreen();

        ArrayList<String> words = Load.loadWords();
        DAWG dawg = new DAWG(words);
        dawg.computeDAWG();
        dawg.toGraphDot("./assets/graph.dot");

        int[][] diagram = dawg.getStateDiagram();
        BitSet finalStates = dawg.getFinalStates();

        FindWords fw = new FindWords(diagram, finalStates);
        int dist = 1;
        String word = "sape"; // sape or saper or falaofe

        ArrayList<String> corr = fw.getWords(dist, word);

        System.out.println(corr.size() + " words found:");
        for (String w : corr) {
            System.out.println(w);
        }

    }
}