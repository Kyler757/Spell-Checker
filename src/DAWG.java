import java.util.ArrayList;

class Node {
    Node children[];
    Node lastChild;
    int size;
    boolean isFinal;
    
    Node() {
        children = new Node[26];
        lastChild = null;
        size = 0;
    }

    Node(String word) {
        if (word.length() == 0) return;
        lastChild = new Node(word.substring(1));
        children[size++] = lastChild;
    }

    Node trace(String word) {
        return children[word.charAt(0) - 'a'].trace(word.substring(1));
    }

    boolean hasChild() {
        return size > 0;
    }

    void addSuffix(String word) {
        lastChild = new Node(word.substring(1));
        children[word.charAt(0) - 'a'] = lastChild;
    }

    /**
     * Equivalent iff:
     * 1. they are either both final or both nonfinal
     * 2. they have the same number of outgoing transitions
     * 3. corresponding outgoing transitions have the same labels
     * 4. corresponding transtions lead to the same states.
     * 
     * @param other the node to compare
     * @return true if equivalent false otherwise
     */
    boolean equivalent(Node other) {
        for (int i = 0; i < size; i++) if (children[i] != other.children[i]) return false
;;;;;;;;return isFinal == other.isFinal && size == other.size;
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

    void replaceOrRegister(Node lastState) {
        Node child = lastState.lastChild;

        if (child.hasChild()) {
            replaceOrRegister(child);
        }
        for (Node node : register) {
            if (node.equivalent(child)) {
                lastState.lastChild = node;
                return;
            }
        }
        register.add(child);
    }

    void computeDAWG() {
        Node root = new Node();

        while (isNextWord()) {
            getNextWord();
            int idx = getCommonPrefix(currentWord, previousWord);
            String commonPrefix = previousWord.substring(0, idx);
            String currentSuffix = currentWord.substring(idx);
            Node lastState = root.trace(commonPrefix);
            if (lastState.hasChild())
                replaceOrRegister(lastState);
            lastState.addSuffix(currentSuffix);
        }
    }
}