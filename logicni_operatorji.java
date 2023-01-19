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
        System.out.println(inputFoo("BIN 101010 AND BIN 111000") + "\n");
        System.out.println(inputFoo("NEG DEC 42") + "\n");
        System.out.println(inputFoo("BIN 1000101 NOR BIN 1110010 AND BIN 1111111") + "\n");
        System.out.println(inputFoo("NEG ( BIN 101010 AND BIN 111000 )") + "\n");
        System.out.println(inputFoo("NEG ( BIN 101010 OR BIN 111000 AND BIN 101000 )") + "\n");
        System.out.println(inputFoo("NEG ( BIN 101010 XOR BIN 111000 NAND ( BIN 101000 OR BIN 01011010 ) )") + "\n");
        System.out.println(inputFoo("NEG DEC 42 AND ( HEX 2A OR OCT 0 )") + "\n");
        System.out.println(inputFoo("( NEG DEC 42 AND BIN 010101 OR ( HEX 2A OR OCT 0 ) )") + "\n");
        // System.out.println(inputFoo(null));
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
        finalAnswer.append(command + " ");
        finalAnswer.append(" = ");
        finalAnswer.append(getAnswer(commands));
        return finalAnswer.toString();
    }

    private static String getAnswer(String[] commands) {
        String answer = null;
        BoolElement defaultEquation = new BoolElement();
        int l = commands.length;
        defaultEquation.setBoundries(0, l - 1);
        String[] parenthesis = new String[l]; // vrzi ven to
        int[] arrayParenCounter = new int[l];
        int counterParen = 0;
        int[] operations = new int[l];
        int[] isDone = new int[l];
        for (int i = 0; i < isDone.length; i++) { // vse vrednosti isDone nastavimo na 0
            isDone[i] = 0;
            if (commands[i].equals("(")) {
                parenthesis[i] = commands[i];
                counterParen++;
                arrayParenCounter[i] = counterParen;
                operations[i] = -1;
            } else if (commands[i].equals(")")) {
                parenthesis[i] = commands[i];
                arrayParenCounter[i] = counterParen;
                counterParen--;
                operations[i] = -1;
            } else if (commands[i].equals("AND")) {
                operations[i] = 3;
                arrayParenCounter[i] = counterParen;
            } else if (commands[i].equals("OR") || commands[i].equals("NOR")) {
                operations[i] = 2;
                arrayParenCounter[i] = counterParen;
            } else if (commands[i].equals("XOR")) {
                operations[i] = 1;
                arrayParenCounter[i] = counterParen;
            } else {
                arrayParenCounter[i] = counterParen;
                operations[i] = -1;
            }
        }
        boolean allIsDone = false;
        int index = 0;
        /*
         * for (int i = 0; i < l; i++) {
         * System.out.print(commands[i] + " ");
         * System.out.print(parenthesis[i] + " ");
         * System.out.println(arrayParenCounter[i] + " ");
         * }
         */
        // get value
        answer = getValue(commands, defaultEquation, operations, arrayParenCounter, answer, 0, isDone);
        return design(answer);
    }

    private static String getValue(String[] commands, logicni_operatorji.BoolElement equation,
            int[] operations, int[] arrayParenCounter, String answer, int prevOpImportance, int[] isDone) {
        BoolElement newSubElement = new BoolElement();
        String tempAnswer = "";
        switch (commands[equation.start]) {
            case "(":
                int tempVal = -1;
                tempVal = findParent(equation.start + 1, equation, arrayParenCounter);
                newSubElement.setBoundries(equation.start + 1, tempVal - 1);
                isDone[equation.start] = 1;
                isDone[tempVal] = 1;
                tempAnswer = getValue(commands, newSubElement, operations, arrayParenCounter, answer, 0, isDone);
                makeDone(isDone, newSubElement.start, newSubElement.end);
                break;
            case "NEG":
                if (commands[equation.start + 1].equals("(")) {
                    tempVal = -1;
                    tempVal = findParent(equation.start + 1, equation, arrayParenCounter);
                    newSubElement.setBoundries(equation.start + 1, equation.end);
                    isDone[equation.start] = 1;
                    tempAnswer = negate(
                            getValue(commands, newSubElement, operations, arrayParenCounter, answer, 0, isDone));
                } else {
                    tempAnswer = negate(computeValue(commands, equation.start + 1));
                    isDone = makeDone(isDone, equation.start, equation.start + 2);
                }
                break;
            case "AND":
                if (commands[equation.start + 1].equals("(")) {
                    tempVal = -1;
                    tempVal = findParent(equation.start + 1, equation, arrayParenCounter);
                    newSubElement.setBoundries(equation.start + 1, tempVal - 1);
                    isDone[equation.start] = 1;
                    isDone[tempVal] = 1;
                    tempAnswer = conjunction(answer,
                            getValue(commands, newSubElement, operations, arrayParenCounter, answer, 3, isDone));
                    isDone = makeDone(isDone, equation.start, tempVal);
                } else {
                    tempAnswer = conjunction(answer, computeValue(commands, equation.start + 1));
                    isDone = makeDone(isDone, equation.start, equation.start + 2);
                }
                break;

            case "NAND":
                if (commands[equation.start + 1].equals("(")) {
                    tempVal = -1;
                    tempVal = findParent(equation.start + 1, equation, arrayParenCounter);
                    newSubElement.setBoundries(equation.start + 1, tempVal - 1);
                    isDone[equation.start] = 1;
                    isDone[tempVal] = 1;
                    tempAnswer = negate(conjunction(answer,
                            getValue(commands, newSubElement, operations, arrayParenCounter, answer, 3, isDone)));
                    isDone = makeDone(isDone, equation.start, tempVal);
                } else {
                    tempAnswer = negate(conjunction(answer, computeValue(commands, equation.start + 1)));
                    isDone = makeDone(isDone, equation.start + 1, equation.start + 2);
                }
                break;

            case "OR":
                if (nextOperation(equation.start + 1, operations, equation.end, 2)) {
                    newSubElement.setBoundries(equation.start + 1, equation.end);
                    tempAnswer = disjunction(answer,
                            getValue(commands, newSubElement, operations, arrayParenCounter, null, 2, isDone));
                    isDone = makeDone(isDone, equation.start, equation.end);
                } else if (commands[equation.start + 1].equals("(")) {
                    tempVal = -1;
                    tempVal = findParent(equation.start + 1, equation, arrayParenCounter);
                    newSubElement.setBoundries(equation.start + 1, tempVal - 1);
                    isDone[equation.start] = 1;
                    isDone[tempVal] = 1;
                    tempAnswer = disjunction(answer,
                            getValue(commands, newSubElement, operations, arrayParenCounter, answer, 2, isDone));
                    isDone = makeDone(isDone, equation.start, tempVal);
                } else {
                    tempAnswer = disjunction(answer, computeValue(commands, equation.start + 1));
                    isDone = makeDone(isDone, equation.start, equation.start + 2);
                }
                break;

            case "NOR":
                if (nextOperation(equation.start + 1, operations, equation.end, 2)) {
                    newSubElement.setBoundries(equation.start + 1, equation.end);
                    tempAnswer = negate(disjunction(answer,
                            getValue(commands, newSubElement, operations, arrayParenCounter, null, 2, isDone)));
                    isDone = makeDone(isDone, equation.start, equation.end);
                } else if (commands[equation.start + 1].equals("(")) {
                    tempVal = -1;
                    tempVal = findParent(equation.start + 1, equation, arrayParenCounter);
                    newSubElement.setBoundries(equation.start + 1, tempVal - 1);
                    isDone[equation.start] = 1;
                    isDone[tempVal] = 1;
                    tempAnswer = disjunction(answer,
                            getValue(commands, newSubElement, operations, arrayParenCounter, answer, 2, isDone));
                    isDone = makeDone(isDone, equation.start, tempVal);
                } else {
                    tempAnswer = negate(disjunction(answer, computeValue(commands, equation.start + 1)));
                    isDone = makeDone(isDone, equation.start, equation.start + 2);
                }
                break;

            case "XOR":
                newSubElement.setBoundries(equation.start + 1, equation.end);
                tempAnswer = exclusiveDisjunction(answer,
                        getValue(commands, newSubElement, operations, arrayParenCounter, null, 1, isDone));
                makeDone(isDone, equation.start, equation.end);
                break;

            default: // stevilo
                tempAnswer = computeValue(commands, equation.start);
                makeDone(isDone, equation.start, equation.start + 1);
                break;
        }
        int initDoneMate = checkIfDone(isDone, equation.start, equation.end);
        if (initDoneMate > -1) {
            newSubElement.setBoundries(initDoneMate, equation.end);
            tempAnswer = getValue(commands, newSubElement, operations, arrayParenCounter, tempAnswer, prevOpImportance,
                    isDone);
        }
        return design(tempAnswer);
    }

    private static boolean nextOperation(int i, int[] operations, int end, int importance) {
        for (int k = i; k < end; k++) {
            if (operations[k] != -1) {
                if (operations[k] > importance) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int findParent(int start, BoolElement equation, int[] arrayParenCounter) {
        int tempVal = -1;
        for (int i = start + 1; i < equation.end; i++) { // tukaj dobimo kje se element
            if (arrayParenCounter[i] < arrayParenCounter[equation.start + 1]) { // konca z zaklepajem
                tempVal = i - 1;
                break;
            }
        }
        if (tempVal == -1) {
            tempVal = equation.end;
        }
        return tempVal;
    }

    private static int[] makeDone(int[] isDone, int start, int end) {
        for (int j = start; j <= end; j++) {
            isDone[j] = 1;
        }
        return isDone;
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
        return design(commands[index + 1]);
    }

    private static String design(String string) {
        if (string.length() < 16) {
            return design("0" + string);
        }
        return string;
    }

    private static String exclusiveDisjunction(String answer, String value) { // XALI
        int a = Integer.parseInt(design(answer), 2);
        int b = Integer.parseInt(design(value), 2);
        int xorResult = a ^ b;
        return design(Integer.toBinaryString(xorResult));
    }

    private static String disjunction(String answer, String value) { // ALI
        int a = Integer.parseInt(design(answer), 2);
        int b = Integer.parseInt(design(value), 2);
        int orResult = a | b;
        return design(Integer.toBinaryString(orResult));
    }

    private static String conjunction(String answer, String value) { // IN
        int a = Integer.parseInt(design(answer), 2);
        int b = Integer.parseInt(design(value), 2);
        int andResult = a & b;
        return design(Integer.toBinaryString(andResult));
    }

    private static String negate(String value) {
        value = design(value);
        StringBuilder negatedValue = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            negatedValue.append(value.charAt(i) == '0' ? '1' : '0');
        }
        return design(negatedValue.toString());
    }

    private static int checkIfDone(int[] isDone, int start, int end) {
        for (int i = start; i < end; i++) {
            if (isDone[i] == 0) {
                return i;
            }
        }
        return -1;
    }
}
