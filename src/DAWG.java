import java.util.ArrayList;

class Node {
    private Node children[];
    private int lastChildIndex = -1;
    private int size = 0;
    private boolean isFinal;
    
    public Node() {
        children = new Node[26];
    }

    public Node(String word) {
        this();
        addSuffix(word);
    }

    public Node[] getChildren() {
        return children;
    }

    public Node getLastChild() {
        return children[lastChildIndex];
    }

    public void setLastChild(Node node) {
        children[lastChildIndex] = node;
    }

    public Node trace(String word) {
        if (word.length() == 0) return this;
        return children[word.charAt(0) - 'a'].trace(word.substring(1));
    }

    public boolean hasChild() {
        return size > 0;
    }

    public void addSuffix(String word) {
        if (word.length() == 0) return;
        size++;
        lastChildIndex = word.charAt(0) - 'a';
        children[word.charAt(0) - 'a'] = new Node(word.substring(1));
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
    public boolean equivalent(Node other) {
        for (int i = 0; i < size; i++) if (children[i] != other.children[i]) return false
;;;;;;;;return isFinal == other.isFinal && size == other.size;
    }

    protected String display(int depth, boolean isLastChild, String transition) {
        String tab = new String("  ");
        String tabs = tab.repeat(depth + 1);
        String out = tab.repeat(depth);

        if (transition.length() > 0) out += "\"" + transition + "\": ";
        out += "{\n";

        out += tabs + "isFinal: " + isFinal + ",\n";
        out += tabs + "isLastChild: " + isLastChild + ",\n";
        out += tabs + "children: {";
        
        if (size > 0) {
            out += "\n";
            for (int i = 0; i < 26; i++) {
                Node child = children[i];
                if (child == null) continue;
                String c = String.valueOf((char) ('a' + i));
                out += child.display(depth + 2, child == children[lastChildIndex], c) + ",\n";
            }
            out = out.substring(0, out.length() - 2);
            out += "\n" + tabs;
        }

        out += "}\n";
        out += tab.repeat(depth) + "}";
        
        return out;
    }

    public String toString() {
        return display(0, false, "");
    }
}

public class DAWG {
    private ArrayList<String> words;
    private ArrayList<Node> register;
    private int currentWordIndex;
    private String currentWord;
    private String previousWord;
    private Node root;

    public DAWG(ArrayList<String> words) {
        this.words = words;
        register = new ArrayList<Node>();
        currentWordIndex = 0;
        currentWord = "";
        previousWord = "";
    }

    public boolean isNextWord() {
        return currentWordIndex < words.size();
    }

    public String getNextWord() {
        previousWord = currentWord;
        return (currentWord = words.get(currentWordIndex++));
    }

    public int getCommonPrefix(String a, String b) {
        for (int i = 0; i < Math.min(a.length(), b.length()); i++) {
            char c0 = a.charAt(i);
            char c1 = b.charAt(i);
            if (c0 != c1) return i;
        }

        return 0;
    }

    public void replaceOrRegister(Node lastState) {
        Node child = lastState.getLastChild();

        if (child.hasChild()) {
            replaceOrRegister(child);
        }
        for (Node node : register) {
            if (node.equivalent(child)) {
                lastState.setLastChild(node);
                return;
            }
        }
        register.add(child);
    }

    public void computeDAWG() {
        root = new Node();

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

    public String toString() {
        return root.toString();
    }
}