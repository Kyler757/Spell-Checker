import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;


public class FindWords {
    public int[][] dawg;
    public BitSet finalStates;

    private int[][] table;
    private int[] chars;
    private HashSet<String>[] paths;


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
    public ArrayList<String> getWords(int dist, String word, int range) {
        int len = word.length() + 1;
        
        chars = new int[len];
        chars[0] = -1;
        
        for (int i = 1; i < len; i++) {
            chars[i] = word.charAt(i - 1) - 'a';
        }

        ArrayList<String> words = new ArrayList<String>();
        int nStates = dawg.length;
        table = new int[nStates][len];
        paths = new HashSet[nStates];
        for (int i = 0; i < nStates; i++) {
            paths[i] = new HashSet<String>();
        }
        
        // Set all table distances to infinity
        for (int i = 0; i < nStates; i++) {
            table[i][word.length()] = Integer.MAX_VALUE;
        }

        // Set first row distances to 0 1 2 ...
        for (int i = 0; i < len; i++) {
            table[0][i] = i;
        }
        
        // start state is zero
        add(0, "");

        HashSet<Integer> finals = new HashSet<Integer>();
        int editDist = Integer.MAX_VALUE;
        for (int state = 0; state < nStates; state++) {
            if (isFinal(state)) {
                if (table[state][len - 1] == editDist) {
                    finals.add(state);
                }
                else if(table[state][len - 1] <= editDist) {
                    editDist = table[state][len - 1];
                    finals.clear();
                    finals.add(state);
                }
            }
        }

        for (int state : finals) {
            HashSet<String> lists = paths[state];
            for (String s : lists) words.add(s);
        }
        
        
        // for (int i = 0; i < table.length; i++) {
        //     System.out.print(i + ": ");
        //     for (int j = 0; j < table[i].length; j++) {
        //         System.out.print(table[i][j] + " ");
        //     }
        //     System.out.println();
        // }

        System.out.println("Edit distance: " + editDist);
        return words;
    }

    private void add(int state, String word) {
        int[] top = table[state];
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

                if (table[nextState][table[0].length - 1] >= arr[arr.length - 1]) {
                    if (table[nextState][table[0].length - 1] != arr[arr.length - 1]) paths[nextState].clear();
                    table[nextState] = arr;

                    String nextWord = word + (char) (sym + 'a');
                    paths[nextState].add(nextWord);

                    add(nextState, nextWord);
                }
                // for (int i = arr.length - 1; i >= 0; i--) {
                //     if (table[nextState][i] == arr[i]) continue;
                //     if (table[nextState][i] > arr[i]) {
                //         table[nextState] = arr;
                //         next.add(nextState);
                //         prev[nextState][0] = state;
                //         prev[nextState][1] = sym;
                //         break;
                //     }
                // }
            }
        }
    }    
}
