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
        System.out.println(inputFoo("NEG DEC 42 AND ( HEX 2A OR OCT 0 )"));
        //System.out.println(inputFoo("( NEG DEC 42 AND BIN 010101 OR ( HEX 2A OR OCT 0 ) )"));
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
        answer = getValue(commands, defaultEquation, parenthesis, arrayParenCounter, answer, 0);
        return null;
    }

    private static String getValue(String[] commands, logicni_operatorji.BoolElement equation,
            String[] parenthesis, int[] arrayParenCounter, String answer, int prevOpImportance) {
        BoolElement newSubElement = new BoolElement();
        String tempAnswer = "";
        switch (commands[equation.start]) {
            case "(":
                int tempVal = -1;
                for (int i = equation.start + 1; i < equation.end; i++) { // tukaj dobimo kje se element
                    if (arrayParenCounter[i] < arrayParenCounter[equation.start + 1]) { // konca z zaklepajem
                        tempVal = i - 1;
                        break;
                    }
                }
                if (tempVal == -1) {
                    tempVal = equation.end;
                }
                newSubElement.setBoundries(equation.start + 1, tempVal - 1);
                tempAnswer = getValue(commands, equation, parenthesis, arrayParenCounter, answer, 0);
                break;
            case "NEG":
                if (commands[equation.start].equals("(")) {
                    newSubElement.setBoundries(equation.start + 1, equation.end);
                    tempAnswer = negate(getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 0));
                } else {
                    tempAnswer = negate(computeValue(commands, equation.start + 1));
                }
                break;
            case "AND":
<<<<<<< HEAD
                if (commands[equation.start + 1].equals("(")) {
                    tempAnswer = conjunction(answer,
                            getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 3));
                } else {
                    tempAnswer = conjunction(answer, computeValue(commands, equation.start + 1));
                }
                break;

            case "NAND":
                if (commands[equation.start + 1].equals("(")) {
                    tempAnswer = negate(conjunction(answer,
                            getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 3)));
                } else {
                    tempAnswer = negate(conjunction(answer, computeValue(commands, equation.start + 1)));
                }
                break;

            case "OR":
                tempAnswer = disjunction(answer,
                        getValue(commands, newSubElement, parenthesis, arrayParenCounter, null, 2));
                break;

            case "NOR":
                tempAnswer = negate(disjunction(answer,
                        getValue(commands, newSubElement, parenthesis, arrayParenCounter, null, 2)));
                break;

            case "XOR":
                tempAnswer = exclusiveDisjunction(answer,
                        getValue(commands, newSubElement, parenthesis, arrayParenCounter, null, 1));
                break;

            default:
=======
            newSubElement.setBoundries(equation.start + 1, equation.start + 2);
            String operand1 = getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 2);
            newSubElement.setBoundries(equation.start + 3, equation.end);
            String operand2 = getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 2);
            tempAnswer = conjunction(operand1, operand2);
            break;
        case "NAND":
            newSubElement.setBoundries(equation.start + 1, equation.start + 2);
            operand1 = getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 2);
            newSubElement.setBoundries(equation.start + 3, equation.end);
            operand2 = getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 2);
            tempAnswer = nonAnd(operand1, operand2);
            break;
        case "NOR":
            newSubElement.setBoundries(equation.start + 1, equation.start + 2);
            operand1 = getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 2);
            newSubElement.setBoundries(equation.start + 3, equation.end);
            operand2 = getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 2);
            tempAnswer = nonOr(operand1, operand2);
            break;
        case "XOR":
            newSubElement.setBoundries(equation.start + 1, equation.start + 2);
            operand1 = getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 2);
            newSubElement.setBoundries(equation.start + 3, equation.end);
            operand2 = getValue(commands, newSubElement, parenthesis, arrayParenCounter, answer, 2);
            tempAnswer = exclusiveDisjunction(operand1, operand2);
            break;

            default: //OR/ALI
		            int a = Integer.parseInt(tempAnswer, 2);
		            int b = Integer.parseInt(getValue(commands, newSubElement, parenthesis, arrayParenCounter, "", prevOpImportance), 2);
		            int orResult = a | b;
		            tempAnswer = Integer.toBinaryString(orResult);
		            break;
>>>>>>> d66aaae794acc9678df26ade0cc3d8c7da5a07e5
                break;
        }
        return null;
    }

    private static void preveri(boolean b, String s) {
        if (!b) {
            throw new IllegalArgumentException("Illegal exception " + s);
        }
    }

    private static String computeValue(String[] commands, int index) { // dobis index kje v 'commands' se nahaja "BIN",
                                                                       // za njim pa stevilo: pretvori ta string v BIN
                                                                       // in potem vrni
        if (!commands[index].equals("BIN")) {
            String retVal = stevilskiSistemi.input(commands[index] + " " + commands[index + 1] + " BIN = ");
            commands[index] = "BIN";
            String[] a = retVal.split(" ");
            return design(a[a.length - 1]);
        }
        return commands[index + 1];
    }

    private static String design(String string) {
        if (string.length() < 16) {
            return design("0" + string);
        }
        return string;
    }

    private static String exclusiveDisjunction(String answer, String value) { //XALI
    int a = Integer.parseInt(answer, 2);
    int b = Integer.parseInt(value, 2);
    int xorResult = a ^ b;
    return Integer.toBinaryString(xorResult);
    }

    private static String disjunction(String answer, String value) { //ALI
        int a = Integer.parseInt(answer, 2);
        int b = Integer.parseInt(value, 2);
        int orResult = a | b;
        return Integer.toBinaryString(orResult);
    }

    private static String conjunction(String answer, String value) { //IN
        int a = Integer.parseInt(answer, 2);
        int b = Integer.parseInt(value, 2);
        int andResult = a & b;
        return Integer.toBinaryString(andResult);
    }
    private static String nonAnd(String operand1, String operand2) { //NAND
        int a = Integer.parseInt(operand1, 2);
        int b = Integer.parseInt(operand2, 2);
        int nandResult = ~(a & b);
        return Integer.toBinaryString(nandResult);
    }

<<<<<<< HEAD
    private static String negate(String value) { // funkcija dobi nek string "101010" ki ga more negirat, vrne string
        return null;
=======
    private static String nonOr(String operand1, String operand2) { //NOR
        int a = Integer.parseInt(operand1, 2);
        int b = Integer.parseInt(operand2, 2);
        int norResult = ~(a | b);
        return Integer.toBinaryString(norResult);
>>>>>>> d66aaae794acc9678df26ade0cc3d8c7da5a07e5
    }



    private static String negate(String value) {
        StringBuilder negatedValue = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            negatedValue.append(value.charAt(i) == '0' ? '1' : '0');
        }
        return negatedValue.toString();
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


