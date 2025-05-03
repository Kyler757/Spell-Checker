import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

class Node {
    private Node children[];
    private int lastChildIndex = -1;
    private int size = 0;
    private int id = 0;
    protected boolean isFinal;
    static int numNodes = 0;
    
    public Node() {
        children = new Node[26];
        id = numNodes++;
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

    public int getId() {
        return id;
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
        Node child = new Node(word.substring(1));
        child.isFinal = word.length() == 1;
        size++;
        lastChildIndex = word.charAt(0) - 'a';
        children[word.charAt(0) - 'a'] = child;
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
        if (size != other.size || isFinal != other.isFinal) return false;
        for (int i = 0; i < 26; i++) if (children[i] != other.children[i]) return false
;;;;;;;;return true;
    }

    public String display(int depth, boolean isLastChild, String transition) {
        String tab = new String("  ");
        String tabs = tab.repeat(depth + 1);
        String out = tab.repeat(depth);

        if (transition.length() > 0) out += "\"" + transition + "\": ";
        out += "{\n";

        out += tabs + "\"id\": " + id + ",\n";
        out += tabs + "\"isFinal\": " + isFinal + ",\n";
        out += tabs + "\"isLastChild\": " + isLastChild + ",\n";
        out += tabs + "\"children\": {";
        
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
        String out = "";
        
        String outTransitions = "";
        for (int i = 0; i < 26; i++) {
            if (children[i] != null) outTransitions += String.valueOf((char) ('a' + i));
        }

        out += "{\n";
        out += "  \"id\": " + id + ",\n";
        out += "  \"isFinal\": " + isFinal + "\n";
        out += "  \"outgoingTransitions\": \"" + outTransitions + "\"\n";
        out += "}";
        
        return out;
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
        int len = Math.min(a.length(), b.length());
        int i = 0;

        for (; i < len; i++) {
            char c0 = a.charAt(i);
            char c1 = b.charAt(i);
            if (c0 != c1) break;
        }

        return i;
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

        replaceOrRegister(root);
    }

    public void printRegister() {
        if (register.size() == 0) { System.out.println("0: []"); return; }
        String out = register.size() + ": [";
        for (Node node : register)
            out += node.getId() + ",";
        out = out.substring(0, out.length() - 1) + "]";
        System.out.println(out);
    }

    public String toString() {
        return root.display(0, false, "");
    }

    /**
     * Converts DAWG to GraphViz format
     * https://csacademy.com/app/graph_editor/
     * 
     * @return
     */
    public void toGraphTxt(String filename) {
        String out = "";
        Stack<Node> stack = new Stack<Node>();

        stack.push(root);

        while (!stack.isEmpty()) {
            Node node = stack.pop();
            
            // Append edges
            Node[] children = node.getChildren();
            for (int i = 0; i < 26; i++) {
                Node child = children[i];
                if (child == null) continue;
                String c = String.valueOf((char) ('a' + i));
                out += node.getId() + " " + child.getId() + " " + c + "\n";
                stack.push(child);
            }
        }

        // Output to file
        File outputFile = new File(filename);
        outputFile.delete();

        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            PrintWriter writer = new PrintWriter(outputFile);
            writer.println(out);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String toGraphDot(String filename) {
        StringBuilder out = new StringBuilder();
        out.append("digraph G {\n");

        Stack<Node> stack = new Stack<>();
        stack.push(root);

        // To avoid revisiting nodes
        HashSet<Node> visited = new HashSet<>();

        while (!stack.isEmpty()) {
            Node node = stack.pop();
            if (!visited.add(node)) continue;

            Node[] children = node.getChildren();
            for (int i = 0; i < 26; i++) {
                Node child = children[i];
                if (child == null) continue;
                String c = String.valueOf((char) ('a' + i));
                out.append("  ")
                .append("\"").append(node.getId()).append("\"")
                .append(" -> ")
                .append("\"").append(child.getId()).append("\"")
                .append(" [label=\"").append(c).append("\"];\n");
                stack.push(child);
            }
        }

        out.append("}\n");

        // Write to file
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toString();
    }
}