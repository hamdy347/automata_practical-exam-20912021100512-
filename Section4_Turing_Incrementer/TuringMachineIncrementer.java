import java.util.*;
//Write a program to simulate a Turing Machine that increments a binary number by
//1
public class TuringMachineIncrementer {

    static class TuringMachine {
        char[] tape;
        int head;
        final char BLANK = '_';

        public TuringMachine(String input) {
            // Allocate extra space for carry overflow
            tape = new char[input.length() + 2];
            Arrays.fill(tape, BLANK);

            // Load input to tape starting from index 1
            for (int i = 0; i < input.length(); i++) {
                tape[i + 1] = input.charAt(i);
            }

            // Start at rightmost bit
            head = input.length();
        }

        public void run() {
            boolean halt = false;

            while (!halt) {
                switch (tape[head]) {
                    case '1':
                        tape[head] = '0';
                        head--;  // carry continues
                        break;
                    case '0':
                        tape[head] = '1';
                        halt = true;
                        break;
                    case BLANK:
                        tape[head] = '1';  // prepend 1 if all were 1s
                        halt = true;
                        break;
                }
            }
        }

        public String getOutput() {
            StringBuilder result = new StringBuilder();
            for (char c : tape) {
                if (c != BLANK) {
                    result.append(c);
                }
            }
            return result.toString();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a binary number (e.g., 1011): ");
        String input = scanner.nextLine();

        if (!input.matches("[01]+")) {
            System.out.println("Invalid binary number.");
            return;
        }

        TuringMachine tm = new TuringMachine(input);
        tm.run();
        System.out.println("Output after increment: " + tm.getOutput());
    }
}
