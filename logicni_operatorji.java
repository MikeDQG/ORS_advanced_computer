import java.util.ArrayList;
import java.util.Scanner;

public class logicni_operatorji {

    static public class BoolElement {
        int start;
        int end;
        ArrayList<String> subElement;

        void setBoundries(int a, int b) {
            this.start = a;
            this.end = b;
        }
    }

    public static void main(String[] args) {
        System.out.println(inputFoo("( NEG DEC 42 AND BIN 010101 OR ( HEX 2A OR OCT 0 ) )"));
        System.out.println(inputFoo(null));
    }

    private static String inputFoo(String string) { // vnesi null ce bere iz terminala
        String command = "";
        if (string == null) {
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine();
        } else {
            command = string;
        }
        String[] commands = command.split(" "); // zdaj imamo polje ukazov ene vrstice vnosnega niza
        StringBuilder finalAnswer = new StringBuilder();
        finalAnswer.append(command);
        finalAnswer.append(getAnswer(commands));
        return null;
    }

    private static String getAnswer(String[] commands) {
        String answer = null;
        BoolElement defaultEquation = new BoolElement();
        int l = commands.length;
        defaultEquation.setBoundries(0, l);
        String[] parenthesis = new String[l];
        int[] arrayParenCounter = new int[l];
        int counterParen = 0;
        int[] isDone = new int[l];
        for (int i = 0; i < isDone.length; i++) { // vse vrednosti isDone nastavimo na 0
            isDone[i] = 0;
            if (commands[i].equals("(")) {
                parenthesis[i] = commands[i];
                counterParen++;
                arrayParenCounter[i] = counterParen;
            } else if (commands[i].equals(")")) {
                parenthesis[i] = commands[i];
                arrayParenCounter[i] = counterParen;
                counterParen--;
            } else
                arrayParenCounter[i] = counterParen;
        }
        boolean allIsDone = false;
        int index = 0;
        while (!allIsDone) {
            allIsDone = true;
        }
        for (int i = 0; i < l; i++) {
            System.out.print(commands[i] + " ");
            System.out.print(parenthesis[i] + " ");
            System.out.println(arrayParenCounter[i] + " ");
        }
        // get value
        answer = getValue(commands, defaultEquation, parenthesis, arrayParenCounter, answer);
        return null;
    }

    private static String getValue(String[] commands, logicni_operatorji.BoolElement equation,
            String[] parenthesis, int[] arrayParenCounter, String answer) {
        BoolElement newSubElement = new BoolElement();
        String tempAnswer = "";
        switch (commands[equation.start]) {
            case "(":
                int tempVal = -1;
                for (int i = equation.start+1; i < equation.end; i++) {                    // tukaj dobimo kje se element
                    if (arrayParenCounter[i] < arrayParenCounter[equation.start+1]) {      // konca z zaklepajem
                        tempVal = i -1;
                        break;
                    }
                }
                newSubElement.setBoundries(equation.start+1, tempVal-1);
                tempAnswer = getValue(commands, equation, parenthesis, arrayParenCounter, answer);
            case "NEG":

                break;

            case "AND":

                break;

            case "NAND":

                break;

            case "OR":

                break;

            case "NOR":

                break;

            case "XOR":

                break;

            default:
                break;
        }
        return null;
    }

    private static int checkIfDone(int[] isDone) {
        for (int i = 0; i < isDone.length; i++) {
            if (isDone[i] == 0) {
                return i;
            }
        }
        return -1;
    }
}
