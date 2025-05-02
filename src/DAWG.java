import java.util.ArrayList;

class Node {
    Node() {

    }
}

public class DAWG {
    private ArrayList<String> words;
    private ArrayList<Node> register;
    private int currentWordIndex;
    private String currentWord;
    private String previousWord;

    DAWG() {
        words = new ArrayList<String>();
        register = new ArrayList<Node>();
        currentWordIndex = 0;
        currentWord = "";
        previousWord = "";
    }

    boolean isNextWord() {
        return currentWordIndex < words.size();
    }

    String getNextWord() {
        previousWord = currentWord;
        return (currentWord = words.get(currentWordIndex++));
    }

    int getCommonPrefix(String a, String b) {
        for (int i = 0; i < words.size(); i++) {
            char c0 = a.charAt(i);
            char c1 = b.charAt(i);
            if (c0 != c1) return i;
        }

        return -1;
    }

    void computeDAWG() {
        // Get first word
        getNextWord();

        while (isNextWord()) {
            getNextWord();
            int lastState = getCommonPrefix(currentWord, previousWord);
            
        }
    }
}