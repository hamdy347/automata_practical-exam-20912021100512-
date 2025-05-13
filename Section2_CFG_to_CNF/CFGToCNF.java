import java.util.*;
//Write a program that takes a CFG and converts it to CNF. Output the new set of CNF
//productions
public class CFGToCNF {
    static class Grammar {
        Set<String> variables = new HashSet<>();
        Set<String> terminals = new HashSet<>();
        Map<String, List<List<String>>> productions = new HashMap<>();
        String startSymbol;

        public Grammar(String start) {
            this.startSymbol = start;
        }

        public void addProduction(String lhs, List<String> rhs) {
            variables.add(lhs);
            for (String symbol : rhs) {
                if (Character.isLowerCase(symbol.charAt(0))) terminals.add(symbol);
                else variables.add(symbol);
            }
            productions.computeIfAbsent(lhs, k -> new ArrayList<>()).add(rhs);
        }

        public void convertToCNF() {
            // Step 1: Remove terminals from RHS with length > 1
            Map<String, String> terminalToVariable = new HashMap<>();
            int tempIndex = 1;
            for (String terminal : terminals) {
                String newVar = "T" + tempIndex++;
                terminalToVariable.put(terminal, newVar);
                variables.add(newVar);
                productions.put(newVar, List.of(List.of(terminal)));
            }

            Map<String, List<List<String>>> newProductions = new HashMap<>();
            for (String lhs : productions.keySet()) {
                List<List<String>> newRules = new ArrayList<>();
                for (List<String> rule : productions.get(lhs)) {
                    List<String> newRule = new ArrayList<>();
                    for (String symbol : rule) {
                        if (terminals.contains(symbol) && rule.size() > 1) {
                            newRule.add(terminalToVariable.get(symbol));
                        } else {
                            newRule.add(symbol);
                        }
                    }
                    newRules.addAll(splitToBinary(newRule, tempIndex));
                }
                newProductions.put(lhs, newRules);
            }

            productions = newProductions;
        }

        private List<List<String>> splitToBinary(List<String> rhs, int tempIndex) {
            List<List<String>> result = new ArrayList<>();
            if (rhs.size() <= 2) {
                result.add(rhs);
            } else {
                List<String> current = new ArrayList<>();
                String prev = rhs.get(0);
                for (int i = 1; i < rhs.size() - 1; i++) {
                    String newVar = "X" + tempIndex++;
                    current = List.of(prev, newVar);
                    result.add(current);
                    prev = newVar;
                }
                result.add(List.of(prev, rhs.get(rhs.size() - 1)));
            }
            return result;
        }

        public void printProductions() {
            System.out.println("Productions in CNF:");
            for (String lhs : productions.keySet()) {
                for (List<String> rhs : productions.get(lhs)) {
                    System.out.println(lhs + " → " + String.join(" ", rhs));
                }
            }
        }
    }

    public static void main(String[] args) {
        Grammar grammar = new Grammar("S");

        // Sample CFG
        grammar.addProduction("S", List.of("A", "B"));
        grammar.addProduction("S", List.of("B", "C"));
        grammar.addProduction("A", List.of("B", "A"));
        grammar.addProduction("A", List.of("a"));
        grammar.addProduction("B", List.of("C", "C"));
        grammar.addProduction("B", List.of("b"));
        grammar.addProduction("C", List.of("A", "B"));
        grammar.addProduction("C", List.of("a"));

        grammar.convertToCNF();
        grammar.printProductions();
    }
}
