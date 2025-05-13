import java.util.*;
//Write a program to automatically generate a PDA from a given CFG and simulate it
//on input strings
public class CFGtoPDASimulator {

    static class CFG {
        String startSymbol;
        Map<String, List<List<String>>> productions;

        CFG(String startSymbol) {
            this.startSymbol = startSymbol;
            this.productions = new HashMap<>();
        }

        void addProduction(String lhs, List<String> rhs) {
            productions.computeIfAbsent(lhs, k -> new ArrayList<>()).add(rhs);
        }

        boolean simulate(String input) {
            return simulateHelper(input, 0, new Stack<>(List.of(startSymbol)));
        }

        private boolean simulateHelper(String input, int pos, Stack<String> stack) {
            if (pos == input.length() && stack.isEmpty()) return true;
            if (stack.isEmpty()) return false;

            String top = stack.pop();

            // Case 1: top of stack is terminal
            if (top.length() == 1 && Character.isLowerCase(top.charAt(0))) {
                if (pos < input.length() && input.charAt(pos) == top.charAt(0)) {
                    return simulateHelper(input, pos + 1, (Stack<String>) stack.clone());
                }
                return false;
            }

            // Case 2: top of stack is non-terminal
            if (productions.containsKey(top)) {
                for (List<String> rhs : productions.get(top)) {
                    Stack<String> newStack = (Stack<String>) stack.clone();
                    // Push RHS in reverse order
                    for (int i = rhs.size() - 1; i >= 0; i--) {
                        if (!rhs.get(i).equals("ε")) {
                            newStack.push(rhs.get(i));
                        }
                    }
                    if (simulateHelper(input, pos, newStack)) return true;
                }
            }

            return false;
        }
    }

    public static void main(String[] args) {
        CFG cfg = new CFG("S");

        // Example CFG:
        // S → a S b | ε
        cfg.addProduction("S", List.of("a", "S", "b"));
        cfg.addProduction("S", List.of("ε")); // epsilon production

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter strings to test (type 'exit' to quit):");

        while (true) {
            System.out.print("Input: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            boolean accepted = cfg.simulate(input);
            System.out.println(accepted ? "Accepted" : "Rejected");
        }
    }
}
