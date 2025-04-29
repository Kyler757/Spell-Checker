public class App {
    public static void main(String[] args) throws Exception {
        // 1 Mod 3, 2 Mod 5 NFA
        NFA nfa = new NFA(9, 2);
        nfa.setStartState(0);
        nfa.setAcceptStates(2, 6);
        nfa.addEdge(0, 1, 'e');
        nfa.addEdge(0, 4, 'e');
        nfa.addEdge(1, 1, '0');
        nfa.addEdge(1, 2, '1');
        nfa.addEdge(2, 0, 'e');
        nfa.addEdge(2, 1, '1');
        nfa.addEdge(2, 3, '0');
        nfa.addEdge(3, 2, '0');
        nfa.addEdge(3, 3, '1');
        nfa.addEdge(4, 4, '0');
        nfa.addEdge(4, 5, '1');
        nfa.addEdge(5, 6, '0');
        nfa.addEdge(5, 7, '1');
        nfa.addEdge(6, 0, 'e');
        nfa.addEdge(6, 4, '1');
        nfa.addEdge(6, 8, '0');
        nfa.addEdge(7, 5, '0');
        nfa.addEdge(7, 6, '1');
        nfa.addEdge(8, 7, '0');
        nfa.addEdge(8, 8, '1');
        nfa.compute();

        System.out.println(nfa.printStartState());
        System.out.println(nfa.printTest("101"));
        System.out.println(nfa.printStateDiagram());
    }
}