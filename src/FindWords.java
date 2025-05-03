import java.util.ArrayList;

public class FindWords {
    public int[][] dawg;
    
    /**
     * Constructor for the FindWords class
     * 
     * @param dawg NxM matrix where N is the number of nodes
     *             and M is the number letters in the alphabet
     *             and each entry is the next state.
     */
    FindWords(int[][] dawg) {
        this.dawg = dawg;
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
