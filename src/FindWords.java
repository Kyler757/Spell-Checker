import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;


public class FindWords {
    public int[][] dawg;
    public BitSet finalStates;

    private int[][] table;
    private int[][] prev;
    private int[] chars;


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
        int len = word.length() + 1;
        this.chars = new int[len];
        this.chars[0] = -1;
        for (int i = 1; i < len; i++) {
            chars[i] = word.charAt(i - 1) - 'a';
        }

        ArrayList<String> words = new ArrayList<String>();
        int m = dawg.length;
        int n = dawg[0].length;
        table = new int[m][word.length() + 1];
        prev = new int[m][2];
        prev[0][0] = -1;
        for (int i = 0; i < m; i++) {
            table[i][word.length()] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < len; i++) {
            table[0][i] = i;
        }

        // for (int i = 0; i < table.length; i++) {
        //     System.out.print(i + ": ");
        //     for (int j = 0; j < table[i].length; j++) {
        //         System.out.print(table[i][j] + " ");
        //     }
        //     System.out.println();
        // }
        
        // start state is zero
        HashSet<Integer> curr = new HashSet<Integer>();
        curr.add(0);
        while (!curr.isEmpty()){
            HashSet<Integer> nex = new HashSet<Integer>();
            for (int state : curr) add(state, nex);
            curr = nex;
        }

        HashSet<Integer> finals = new HashSet<Integer>();
        int big = Integer.MAX_VALUE;
        for (int state = 0; state < m; state++) {
            if (isFinal(state)) {
                if (table[state][len - 1] == big) {
                    finals.add(state);
                }
                else if(table[state][len - 1] <= big) {
                    big = table[state][len - 1];
                    finals.clear();
                    finals.add(state);
                }
            }
        }
        // System.out.println(finals);

        for (int state : finals) words.add(getWord(state));
        for (int i = 0; i < table.length; i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(big);
        return words;
    }

    private void add(int state, HashSet<Integer> next) {
        int[] top = table[state];
        for (int sym = 0; sym < dawg[state].length; sym++) {
            if (dawg[state][sym] != -1) {
                int nextState = dawg[state][sym];
                int[] arr = new int[chars.length];
                arr[0] = top[0] + 1;

                for (int i = 1; i < chars.length; i++) {
                    int s = Math.min(Math.min(top[i-1], top[i]), arr[i-1]);
                    if (chars[i] == sym) arr[i] = s;
                    else arr[i] = s + 1;
                }
                // if (nextState == 5) {
                //     System.out.println("test");
                //     System.out.print("[" + state + "] ");
                //     for (int j = 0; j < arr.length; j++) {
                //         System.out.print(arr[j] + " ");
                //     }
                //     System.out.println();
                //     System.out.print("[" + nextState + "] ");
                //     for (int j = 0; j < table[nextState].length; j++) {
                //         System.out.print(table[nextState][j] + " ");
                //     }
                //     System.out.println();
                // }
                if (table[nextState][table[0].length - 1] > arr[arr.length - 1]) {
                    table[nextState] = arr;
                    next.add(nextState);
                    prev[nextState][0] = state;
                    prev[nextState][1] = sym;
                }
            }
        }
    }

    private String getWord(int state) {
        StringBuilder sb = new StringBuilder();
        int i = state;
        while (state != 0) {
            int sym = prev[state][1];
            sb.append((char) (sym + 'a'));
            state = prev[state][0];
        }
        return sb.reverse().toString();
    }
        
}
