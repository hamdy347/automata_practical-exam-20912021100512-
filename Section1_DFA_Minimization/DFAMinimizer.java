import java.util.*;
//Write a program that minimizes a given DFA using the partition refinement method
public class DFAMinimizer {

    static class DFA {
        Set<String> states;
        Set<Character> alphabet;
        Map<String, Map<Character, String>> transitions;
        String startState;
        Set<String> acceptStates;

        public DFA(Set<String> states, Set<Character> alphabet,
                   Map<String, Map<Character, String>> transitions,
                   String startState, Set<String> acceptStates) {
            this.states = states;
            this.alphabet = alphabet;
            this.transitions = transitions;
            this.startState = startState;
            this.acceptStates = acceptStates;
        }

        public void minimize() {
            Set<Set<String>> partitions = new HashSet<>();
            Set<String> nonAccepting = new HashSet<>(states);
            nonAccepting.removeAll(acceptStates);

            partitions.add(acceptStates);
            partitions.add(nonAccepting);

            boolean changed = true;
            while (changed) {
                changed = false;
                Set<Set<String>> newPartitions = new HashSet<>();

                for (Set<String> group : partitions) {
                    Map<String, Set<String>> splitter = new HashMap<>();

                    for (String state : group) {
                        StringBuilder key = new StringBuilder();
                        for (char c : alphabet) {
                            String dest = transitions.get(state).get(c);
                            Set<String> targetGroup = getGroupContaining(dest, partitions);
                            key.append(partitions.hashCode()).append(targetGroup.hashCode());
                        }
                        splitter.computeIfAbsent(key.toString(), k -> new HashSet<>()).add(state);
                    }

                    newPartitions.addAll(splitter.values());
                    if (splitter.size() > 1) changed = true;
                }

                partitions = newPartitions;
            }

            System.out.println("\nMinimized DFA States (Partitions):");
            for (Set<String> group : partitions) {
                System.out.println(group);
            }
        }

        private Set<String> getGroupContaining(String state, Set<Set<String>> partitions) {
            for (Set<String> group : partitions) {
                if (group.contains(state)) return group;
            }
            return null;
        }
    }

    public static void main(String[] args) {
        // DFA definition
        Set<String> states = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E", "F"));
        Set<Character> alphabet = new HashSet<>(Arrays.asList('0', '1'));

        Map<String, Map<Character, String>> transitions = new HashMap<>();
        transitions.put("A", Map.of('0', "B", '1', "C"));
        transitions.put("B", Map.of('0', "A", '1', "D"));
        transitions.put("C", Map.of('0', "E", '1', "F"));
        transitions.put("D", Map.of('0', "E", '1', "F"));
        transitions.put("E", Map.of('0', "E", '1', "F"));
        transitions.put("F", Map.of('0', "F", '1', "F"));

        String startState = "A";
        Set<String> acceptStates = new HashSet<>(Arrays.asList("E", "F"));

        DFA dfa = new DFA(states, alphabet, transitions, startState, acceptStates);

        System.out.println("Original DFA States: " + states);
        dfa.minimize();
    }
}
