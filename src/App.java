import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws Exception {
        Print.clearScreen();

        File inputFile = new File("../sorted_output.csv");
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
        dawg.toGraphDot("graph.dot");
    }
}