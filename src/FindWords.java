import java.util.ArrayList;
import java.util.BitSet;

public class FindWords {
    public int[][] dawg;
    public BitSet finalStates;

    /**
     * Constructor for the FindWords class
     * 
     * @param dawg NxM matrix where N is the number of nodes
     *             and M is the number letters in the alphabet
     *             and each entry is the next state.
     * @param finalStates List of final states
     */
    FindWords(int[][] dawg, BitSet finalStates) {
        this.dawg = dawg;
        this.finalStates = finalStates;
    }

    /**
     * Checks if a given state is a final state
     *
     * @param state The state
     * @return True if the state is a final state and false otherwise
     */
    boolean isFinal(int state) {
        return finalStates.get(state);
    }

    /**
     * Gets all words of a given edit distance
     * 
     * @param dist The edit distance
     * @param word The word to start from
     * @return A list of words within the edit distance
     */
    public ArrayList<String> getWords(int dist, String word) {
        ArrayList<String> words = new ArrayList<String>();
        return words;
    }
}
