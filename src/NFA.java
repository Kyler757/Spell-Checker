import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

class NFA {
    private final int NUM_STATES;
    private final int N_ALPHABET;
    private List<Integer>[] E_TRANS;
    private List<Integer>[][] A_TRANS;
    private int[][] STATE_DIAGRAM;
    private int[] COMPUTED_E_TRANS;
    private int startState = 0;
    private int acceptStates = 0;

    @SuppressWarnings("unchecked")
    public NFA(Number numStates, Number nAlphabet) {
        NUM_STATES = numStates.intValue();
        N_ALPHABET = nAlphabet.intValue();
        E_TRANS = new ArrayList[(int) NUM_STATES];
        A_TRANS = new ArrayList[(int) NUM_STATES][(int) N_ALPHABET];
        STATE_DIAGRAM = new int[(int) NUM_STATES][(int) N_ALPHABET];
        COMPUTED_E_TRANS = new int[(int) NUM_STATES];

        for (int i = 0; i < (int) NUM_STATES; i++) {
            E_TRANS[i] = new ArrayList<>();
            for (int j = 0; j < (int) N_ALPHABET; j++) {
                A_TRANS[i][j] = new ArrayList<>();
            }
        }
    }

    public void addEdge(int from, int to, char c) {
        if (c == 'e') {
            E_TRANS[from].add(to);
        } else if (c >= '0' && c < '0' + (int) N_ALPHABET) {
            A_TRANS[from][c - '0'].add(to);
        } else {
            throw new IllegalArgumentException("Invalid character");
        }
    }

    public void addEdge(int from, int to, char... chars) {
        for (char c : chars) {
            addEdge(from, to, c);
        }
    }

    public void compute() {
        for (int state = 0; state < (int) NUM_STATES; state++) {
            for (int symbol = 0; symbol < (int) N_ALPHABET; symbol++) {
                for (int nextState : A_TRANS[state][symbol]) {
                    resetEpsilonFlags();
                    STATE_DIAGRAM[state][symbol] |= getConnectedStates(nextState);
                }
            }
        }

        resetEpsilonFlags();
        startState = getConnectedStates(Integer.numberOfTrailingZeros(startState));
    }

    public void setAcceptStates(int... states) {
        for (int state : states) {
            acceptStates |= (1 << state);
        }
    }

    public void setStartState(int state) {
        startState = (1 << state);
    }

    public boolean test(String str) {
        int finalState = deltaStar(startState, str);
        return (finalState & acceptStates) != 0;
    }

    public String printTest(String str) {
        int finalState = deltaStar(startState, str);
        String result = (finalState & acceptStates) != 0 ? "Accepted" : "Rejected";
        return result + ": " + BitSet.valueOf(new long[]{finalState}).toString();
    }

    public String printStartState() {
        return BitSet.valueOf(new long[]{startState}).toString();
    }

    public String printStateDiagram() {
        StringBuilder diagram = new StringBuilder();

        for (int i = 0; i < (int) NUM_STATES; i++) {
            for (int j = 0; j < (int) N_ALPHABET; j++) {
                diagram.append(BitSet.valueOf(new long[]{STATE_DIAGRAM[i][j]}).toString()).append(" ");
            }
            diagram.append("\n");
        }

        return diagram.toString();
    }

    private int deltaStar(int currentState, String str) {
        int nextState = 0;

        for (char c : str.toCharArray()) {
            int bit = c - 'a';

            for (int j = 0; currentState != 0; j++, currentState >>= 1) {
                if ((currentState & 1) != 0) {
                    nextState |= STATE_DIAGRAM[j][bit];
                }
            }

            currentState = nextState;
            nextState = 0;
        }

        return currentState;
    }

    private void resetEpsilonFlags() {
        Arrays.fill(COMPUTED_E_TRANS, 0);
    }

    private int getConnectedStates(int state) {
        if (COMPUTED_E_TRANS[state] != 0) return 1 << state;

        COMPUTED_E_TRANS[state] = 1;

        int connectedStates = 1 << state;

        for (int next : E_TRANS[state]) {
            connectedStates |= getConnectedStates(next);
        }

        return connectedStates;
    }
}