package src;
import java.util.ArrayList;
import java.util.BitSet;
import src.model.DAWG;
import src.model.FindWords;
import src.model.Load;
import src.model.Print;

public class App {
    public static void main(String[] args) throws Exception {
        Print.clearScreen();

        ArrayList<String> words = Load.loadWords();
        DAWG dawg = new DAWG(words);
        dawg.computeDAWG();
        dawg.toGraphDot("./assets/graph.dot");

        int[][] diagram = dawg.getStateDiagram();
        BitSet finalStates = dawg.getFinalStates();

        // Time to test the FindWords class
        var start = System.currentTimeMillis();        

        FindWords fw = new FindWords(diagram, finalStates);
        System.out.println(diagram.length + " states found.");
        int dist = 1;
        String word = "col"; // sape or saper falaofe  maby

        ArrayList<String> corr = fw.getWords(dist, word, 1, true);

        var end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "ms");

        System.out.println(corr.size() + " words found:");
        for (String w : corr) {
            System.out.println(w);
        }

    }
}