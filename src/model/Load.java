package src.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Load {
    public static ArrayList<String> loadWords() {
        ArrayList<String> words = new ArrayList<>();
        File inputFile = new File("./assets/longEnglishOut.csv");
        Scanner scanner;
        
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            return words;
        }

        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        
        while (scanner.hasNextLine()) {
            String word = scanner.nextLine();
            if (!pattern.matcher(word).matches()) continue;
            words.add(word.toLowerCase());
        }
        
        scanner.close();
        
        return words;
    }
}
