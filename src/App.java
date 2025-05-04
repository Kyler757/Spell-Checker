package src;
import src.dawg.DAWG;
import src.dawg.FindWords;
import src.helper.Print;
import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws Exception {
        Print.clearScreen();

        File inputFile = new File("./assets/sorted_output.csv");
        Scanner scanner = new Scanner(inputFile);
        ArrayList<String> words = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        
        while (scanner.hasNextLine()) {
            String word = scanner.nextLine();
            if (!pattern.matcher(word).matches()) continue;
            words.add(word.toLowerCase());
        }
        
        scanner.close();
        DAWG dawg = new DAWG(words);
        dawg.computeDAWG();
        dawg.toGraphDot("./assets/graph.dot");

        int[][] diagram = dawg.getStateDiagram();
        BitSet finalStates = dawg.getFinalStates();
        
        // Print diagram
        // for (int i = 0; i < diagram.length; i++) {
        //     System.out.print(i + ": ");
        //     for (int j = 0; j < diagram[i].length; j++) {
        //         System.out.print(diagram[i][j] + " ");
        //     }
        //     System.out.println();
        // }
        
        // System.out.println(finalStates);

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