package src.model;
import java.util.ArrayList;
import java.util.BitSet;


public class FindWords {
    public int[][] dawg;
    public BitSet finalStates;

    private int[] chars;
    private int range;
    ArrayList<String>[] ws;
    private int low = Integer.MAX_VALUE / 2;


    /**
     * Constructor for the FindWords class
     * 
     * @param dawg NxM matrix where N is the number of nodes
     *             and M is the number letters in the alphabet
     *             and each entry is the next state.
     * @param finalStates List of final states
     */
    public FindWords(int[][] dawg, BitSet finalStates) {
        this.dawg = dawg;
        this.finalStates = finalStates;
        System.out.println("Loaded DAWG with " + dawg.length + " nodes.");
    }

    /**
     * Checks if a given state is a final state
     *
     * @param state The state
     * @return True if the state is a final state and false otherwise
     */
    public boolean isFinal(int state) {
        return finalStates.get(state);
    }

    /**
     * Gets all words of a given edit distance
     * 
     * @param word The word to start from
     * @param range Include words within the shortest edit distance plus the range
     * @return A list of words within the edit distance
     */
    public ArrayList<String> getWords(String word, int range, boolean firstLetter) {
        this.range = range;
        this.low = Integer.MAX_VALUE / 2;
        int len = word.length() + 1;
        
        ws = new ArrayList[range + 1];
        for (int i = 0; i <= range; i++) {
            ws[i] = new ArrayList<String>();
        }

        chars = new int[len];
        chars[0] = -1;
        
        for (int i = 1; i < len; i++) {
            chars[i] = word.charAt(i - 1) - 'a';
        }

        ArrayList<String> words = new ArrayList<String>();
        
        // start state is zero
        if (firstLetter) {
            int sym = chars[1];
            int first = dawg[0][sym];
            if (first != -1) {
                int[] arr = new int[len];
                arr[0] = 1;
                for (int i = 1; i < len; i++) {
                    int d = i;
                    if (chars[i] == sym) d -= 1;
                    int s = Math.min(Math.min(d, i + 1), arr[i-1] + 1);
                    arr[i] = s;
                }
                add(first, word.charAt(0) + "", arr);
            }
        }
        else {
            int[] arr = new int[len];
            for (int i = 0; i < len; i++) {
                arr[i] = i;
            }
            add(0, "", arr);
        }

        for (int i = 0; i <= range; i++) {
            for (String s : ws[i]) words.add(s);
        }

        return words;
    }

    private void add(int state, String word, int[] top) {
        for (int sym = 0; sym < dawg[state].length; sym++) {
            if (dawg[state][sym] != -1) {
                int nextState = dawg[state][sym];

                // make arr
                int[] arr = new int[chars.length];
                arr[0] = top[0] + 1;
                for (int i = 1; i < chars.length; i++) {
                    int d = top[i-1] + 1;
                    if (chars[i] == sym) d -= 1;
                    int s = Math.min(Math.min(d, top[i] + 1), arr[i-1] + 1);
                    arr[i] = s;
                }

                String nextWord = word + (char) (sym + 'a');
                
                int l = arr[arr.length - 1];
                // add the word to ws if it is final
                if (isFinal(nextState) && l <= low + range) {
                    if (l < low) {
                        int k = low - l;
                        for (int i = range; i >= 0; i--) {
                            if (i - k >= 0) {
                                ws[i]= ws[i - k];
                            }
                            else {
                                ws[i] = new ArrayList<String>();
                            }
                        }
                        low = l;
                        ws[0].add(nextWord);
                    }
                    else {
                        ws[l - low].add(nextWord);
                    }
                }
                add(nextState, nextWord, arr);
            }
        }
    }    
}
