import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.lang.model.element.Element;
import javax.naming.InitialContext;
import javax.sound.sampled.Line;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class computer {

    /*
     * 
     * importance:
     * 4 '/'
     * 3 '*'
     * 2 '+'
     * 2 '-'
     * 
     */

    static class EqElement {
        int start;
        int end;

        ArrayList<String> innerElement = new ArrayList<String>();

        void initialiseElement(int x, int y) {
            this.start = x;
            this.end = y;
        }

        private void setNewElement(ArrayList<String> arrayList, EqElement eqElement) {
            for (int i = eqElement.start; i < eqElement.end; i++) {
                innerElement.add(arrayList.get(i));
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        System.out.println(compute(s));
    }

    private static String compute(String defaultInputString) {
        StringBuilder functionStringBuilder = new StringBuilder();
        ArrayList<String> input = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int counter = 0;
        if (defaultInputString == null) {
            input = readLineFromFile();
            if (input == null) {
                return null;
            } else {
                counter++;
            }
        } else {
            input.add(defaultInputString);
        }
        for (int i = 0; i < input.size(); i++) { // one iteration for every line from the file
            String line = input.get(i);
            String[] commands = line.split(" ");
            functionStringBuilder.append(realComputations(commands));
            functionStringBuilder.append("\n");
        }

        return functionStringBuilder.toString();
    }

    private static String realComputations(String[] commands) {
        StringBuilder answer = new StringBuilder();
        ArrayList<String> equation = new ArrayList();
        for (int i = 0; i < commands.length; i++) {
            answer.append(commands[i] + " ");
            equation.add(commands[i]);
        }
        answer.append(solveEquation(equation));
        return answer.toString();
    }

    private static String solveEquation(ArrayList<String> equation) {
        int l = equation.size();
        int[] numbers = new int[l];
        String[] operators = new String[l];
        String[] possibleOperators = { "+", "-", "*", "/", "(", ")", "=", "pow", "sqrt", "," };
        int[] isDone = new int[l];
        for (int i = 0; i < numbers.length; i++) { // create 'operators' and fill in the operators from 'equation',
                                                   // similarly fill out 'numbers' with numbers from 'equation'
            String key = equation.get(i);
            if (Arrays.stream(possibleOperators).anyMatch(key::equals)) {
                operators[i] = key;
            } else {
                operators[i] = null;
                numbers[i] = Integer.parseInt(key);
            }
        }

        String[] commands = new String[l];
        int[] arrayParenCounter = new int[l];
        int counterParen = 0;
        int[] operations = new int[l];

        for (int i = 0; i < isDone.length; i++) { // vse vrednosti isDone nastavimo na 0
            commands[i] = equation.get(i);
            isDone[i] = 0;
            if (commands[i].equals("(")) {
                counterParen++;
                arrayParenCounter[i] = counterParen;
                operations[i] = -1;
            } else if (commands[i].equals(")")) {
                arrayParenCounter[i] = counterParen;
                counterParen--;
                operations[i] = -1;
            } else if (commands[i].equals("/")) {
                operations[i] = 4;
                arrayParenCounter[i] = counterParen;
            } else if (commands[i].equals("+") || commands[i].equals("-")) {
                operations[i] = 2;
                arrayParenCounter[i] = counterParen;
            } else if (commands[i].equals("*")) {
                operations[i] = 3;
                arrayParenCounter[i] = counterParen;
            } else {
                arrayParenCounter[i] = counterParen;
                operations[i] = -1;
            }
        }

        double answer = 0;
        EqElement eqElement = new EqElement();
        eqElement.end = equation.size() - 1;
        ArrayList<EqElement> collectionOfEqElements = new ArrayList<EqElement>();
        collectionOfEqElements.add(eqElement);
        answer = getValue(equation, eqElement, numbers, operators, possibleOperators, isDone, collectionOfEqElements,
                arrayParenCounter, -1, 0, operations);
        return String.valueOf(answer);
    }

    private static double getValue(ArrayList<String> equation, EqElement eqElement, int[] numbers, String[] operators,
            String[] possibleOperators, int[] isDone, ArrayList<computer.EqElement> collectionOfEqElements,
            int[] arrayParenCounter, int prevOpImportance, double tempAnswer, int[] operations) {
        double answer = 0;
        int parenthesisCounter = 0;
        EqElement newElEqElement = new EqElement();
        EqElement doneElement = new EqElement();
        switch (equation.get(eqElement.start)) {
            case "pow":
                parenthesisCounter = 0;
                int powComa = -1;
                for (int i = eqElement.start + 1; i < operators.length; i++) {
                    if (operators[i] != null) {
                        if (operators[i].equals(")")) {
                            if (parenthesisCounter <= 1) {
                                newElEqElement.start = eqElement.start + 1;
                                newElEqElement.end = i;
                                newElEqElement.setNewElement(equation, newElEqElement);
                                isDone[newElEqElement.start] = 1;
                                isDone[newElEqElement.end] = 1;
                                break;
                            } else {
                                parenthesisCounter--;
                            }
                        } else if (equation.get(i).equals("(")) {
                            parenthesisCounter++;
                        } else if (equation.get(i).equals(",") && parenthesisCounter == 1) {
                            powComa = i;
                        }
                    }
                }
                assert powComa > -1;
                EqElement first = new EqElement();
                EqElement second = new EqElement();
                isDone[eqElement.start] = 1;
                isDone[eqElement.start + 1] = 1;
                isDone[newElEqElement.end] = 1;
                first.initialiseElement(newElEqElement.start + 1, powComa - 1);
                second.initialiseElement(powComa + 1, newElEqElement.end - 1);
                answer = Math.pow(
                        getValue(equation, first, numbers, operators, possibleOperators, isDone,
                                collectionOfEqElements, arrayParenCounter, -1, tempAnswer, operations),
                        getValue(equation, second, numbers, operators, possibleOperators, isDone,
                                collectionOfEqElements, arrayParenCounter, -1, tempAnswer, operations));
                doneElement.initialiseElement(eqElement.start, newElEqElement.end);
                makeDone(isDone, doneElement);
                break;
            case "sqrt":
                int temVal = findParent(eqElement.start, eqElement, arrayParenCounter);
                newElEqElement.initialiseElement(eqElement.start + 2, temVal -1);
                isDone[eqElement.start] = 1;
                isDone[eqElement.start + 1] = 1;
                isDone[temVal] = 1;
                first = new EqElement();
                first.initialiseElement(newElEqElement.start, temVal -1);
                answer = Math.sqrt(getValue(equation, first, numbers, operators, possibleOperators, isDone,
                        collectionOfEqElements, arrayParenCounter, -1, tempAnswer, operations));
                doneElement.initialiseElement(eqElement.start, temVal);
                makeDone(isDone, doneElement);
                break;
            case "(":
                parenthesisCounter = 1;
                for (int i = eqElement.start + 1; i < operators.length; i++) {
                    if (operators[i] != null) {
                        if (operators[i].equals(")")) {
                            if (parenthesisCounter <= 1) {
                                newElEqElement.start = eqElement.start + 1;
                                newElEqElement.end = i;
                                newElEqElement.setNewElement(equation, newElEqElement);
                                isDone[newElEqElement.start] = 1;
                                isDone[newElEqElement.end] = 1;
                                break;
                            } else {
                                parenthesisCounter--;
                            }
                        } else if (equation.get(i).equals("(")) {
                            parenthesisCounter++;
                        }
                    }
                }
                answer = getValue(equation, newElEqElement, numbers, operators, possibleOperators, isDone,
                        collectionOfEqElements, arrayParenCounter, -1, tempAnswer, operations);
                doneElement.initialiseElement(eqElement.start, newElEqElement.end);
                makeDone(isDone, doneElement);
                break;
            case "+":
                if (nextOperation(eqElement.start, operations, eqElement.end, 2)) {
                    newElEqElement.initialiseElement(eqElement.start + 1, eqElement.end);
                    answer = add(tempAnswer,
                            getValue(equation, newElEqElement, numbers, operators, possibleOperators, isDone,
                                    collectionOfEqElements, arrayParenCounter, prevOpImportance, tempAnswer,
                                    operations));
                    isDone = makeDone(isDone, eqElement);
                } else if (equation.get(eqElement.start + 1).equals("(")) {
                    temVal = -1;
                    temVal = findParent(eqElement.start + 1, eqElement, arrayParenCounter);
                    isDone[eqElement.start] = 1;
                    newElEqElement.initialiseElement(eqElement.start + 1, temVal);
                    answer = add(tempAnswer, getValue(equation, newElEqElement, numbers, operators, possibleOperators,
                            isDone, collectionOfEqElements, arrayParenCounter, 2, tempAnswer, operations));
                    doneElement.initialiseElement(eqElement.start, temVal);
                    isDone = makeDone(isDone, doneElement);
                } else {
                    answer = add(tempAnswer, convertToInt(equation.get(eqElement.start + 1)));
                    doneElement.initialiseElement(eqElement.start, eqElement.start + 2);
                    makeDone(isDone, doneElement);
                }
                break;
            case "-":
                if (nextOperation(eqElement.start, operations, eqElement.end, 2)) {
                    newElEqElement.initialiseElement(eqElement.start + 1, eqElement.end);
                    answer = subtract(tempAnswer,
                            getValue(equation, newElEqElement, numbers, operators, possibleOperators, isDone,
                                    collectionOfEqElements, arrayParenCounter, prevOpImportance, tempAnswer,
                                    operations));
                    isDone = makeDone(isDone, eqElement);
                } else if (equation.get(eqElement.start + 1).equals("(")) {
                    temVal = -1;
                    temVal = findParent(eqElement.start + 1, eqElement, arrayParenCounter);
                    isDone[eqElement.start] = 1;
                    newElEqElement.initialiseElement(eqElement.start + 1, temVal);
                    answer = subtract(tempAnswer,
                            getValue(equation, newElEqElement, numbers, operators, possibleOperators, isDone,
                                    collectionOfEqElements, arrayParenCounter, 2, tempAnswer, operations));
                    doneElement.initialiseElement(eqElement.start, temVal);
                    isDone = makeDone(isDone, doneElement);
                } else {
                    answer = subtract(tempAnswer, convertToInt(equation.get(eqElement.start + 1)));
                    doneElement.initialiseElement(eqElement.start, eqElement.start + 2);
                    makeDone(isDone, doneElement);
                }
                break;
            case "*":
                if (nextOperation(eqElement.start, operations, eqElement.end, 3)) {
                    newElEqElement.initialiseElement(eqElement.start + 1, eqElement.end);
                    answer = multiply(tempAnswer,
                            getValue(equation, newElEqElement, numbers, operators, possibleOperators, isDone,
                                    collectionOfEqElements, arrayParenCounter, prevOpImportance, tempAnswer,
                                    operations));
                    isDone = makeDone(isDone, eqElement);
                } else if (equation.get(eqElement.start + 1).equals("(")) {
                    temVal = -1;
                    temVal = findParent(eqElement.start + 1, eqElement, arrayParenCounter);
                    isDone[eqElement.start] = 1;
                    newElEqElement.initialiseElement(eqElement.start + 1, temVal);
                    answer = multiply(tempAnswer,
                            getValue(equation, newElEqElement, numbers, operators, possibleOperators, isDone,
                                    collectionOfEqElements, arrayParenCounter, 2, tempAnswer, operations));
                    doneElement.initialiseElement(eqElement.start, temVal);
                    isDone = makeDone(isDone, doneElement);
                } else {
                    answer = multiply(tempAnswer, convertToInt(equation.get(eqElement.start + 1)));
                    doneElement.initialiseElement(eqElement.start, eqElement.start + 2);
                    makeDone(isDone, doneElement);
                }
                break;

                case "%":
                if (nextOperation(eqElement.start, operations, eqElement.end, 3)) {
                    newElEqElement.initialiseElement(eqElement.start + 1, eqElement.end);
                    answer = remainder(tempAnswer,
                            getValue(equation, newElEqElement, numbers, operators, possibleOperators, isDone,
                                    collectionOfEqElements, arrayParenCounter, prevOpImportance, tempAnswer,
                                    operations));
                    isDone = makeDone(isDone, eqElement);
                } else if (equation.get(eqElement.start + 1).equals("(")) {
                    temVal = -1;
                    temVal = findParent(eqElement.start + 1, eqElement, arrayParenCounter);
                    isDone[eqElement.start] = 1;
                    newElEqElement.initialiseElement(eqElement.start + 1, temVal);
                    answer = remainder(tempAnswer,
                            getValue(equation, newElEqElement, numbers, operators, possibleOperators, isDone,
                                    collectionOfEqElements, arrayParenCounter, 2, tempAnswer, operations));
                    doneElement.initialiseElement(eqElement.start, temVal);
                    isDone = makeDone(isDone, doneElement);
                } else {
                    answer = remainder(tempAnswer, convertToInt(equation.get(eqElement.start + 1)));
                    doneElement.initialiseElement(eqElement.start, eqElement.start + 2);
                    makeDone(isDone, doneElement);
                }
                break;

            case "/":
                if (equation.get(eqElement.start + 1).equals("(")) {
                    temVal = -1;
                    temVal = findParent(eqElement.start + 1, eqElement, arrayParenCounter);
                    isDone[eqElement.start] = 1;
                    newElEqElement.initialiseElement(eqElement.start + 1, temVal);
                    answer = divide(tempAnswer,
                            getValue(equation, newElEqElement, numbers, operators, possibleOperators, isDone,
                                    collectionOfEqElements, arrayParenCounter, 2, tempAnswer, operations));
                    doneElement.initialiseElement(eqElement.start, temVal);
                    isDone = makeDone(isDone, doneElement);
                } else {
                    answer = divide(tempAnswer, convertToInt(equation.get(eqElement.start + 1)));
                    doneElement.initialiseElement(eqElement.start, eqElement.start + 2);
                    makeDone(isDone, doneElement);
                }
                break;
            default:
                assert equation.get(eqElement.start) != null;
                answer = (double) Integer.valueOf(equation.get(eqElement.start));
                doneElement.initialiseElement(eqElement.start, eqElement.start + 1);
                isDone = makeDone(isDone, doneElement);
                break;
        }/*
          * EqElement anothEqElement = new EqElement();
          * int tempInt = newElEqElement.start;
          * anothEqElement.start = tempInt;
          * anothEqElement.start -= 1;
          * tempInt = newElEqElement.end;
          * anothEqElement.end = tempInt;
          * anothEqElement.start += 1;
          * isDone = makeDone(isDone, anothEqElement);
          * anothEqElement.start = newElEqElement.end + 1;
          * anothEqElement.end = eqElement.end;
          * int notDone = checkIfDone(isDone, eqElement);
          * if (notDone != -1) {
          * anothEqElement.start = notDone;
          * }
          */

        int notDone = checkIfDone(isDone, eqElement);
        if (notDone != -1) {
            eqElement.start = notDone;
            answer = getValue(equation, eqElement, numbers, operators, possibleOperators, isDone,
                    collectionOfEqElements, arrayParenCounter, -1, answer, operations);
        }
        return answer;
    }

    private static int convertToInt(String s) {
        return Integer.parseInt(s);
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

    private static int checkIfDone(int[] isDone, computer.EqElement eqElement) {
        for (int i = eqElement.start; i < eqElement.end; i++) {
            if (isDone[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    private static int[] makeDone(int[] isDone, computer.EqElement eqElement) {
        for (int i = eqElement.start; i < eqElement.end; i++) {
            isDone[i] = 1;
        }
        return isDone;
    }

    public static double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        double val = a / b;
        return val;
    }

    public static double multiply(double a, double b) {
        double val = a * b;
        return val;
    }

    public static double remainder(double a, double b) {
        double val = a % b;
        return val;
    }

    public static double subtract(double a, double b) {
        double val = a - b;
        return val;
    }

    public static double add(double tempAnswer, double d) {
        return tempAnswer + d;
    }

    private static int findParent(int start, EqElement equation, int[] arrayParenCounter) {
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

    private static ArrayList<String> readLineFromFile() {
        ArrayList<String> allLines = new ArrayList<>();
        try {
            File myObj = new File("racunanje.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                // System.out.println(data);
                allLines.add(data);
            }
            myReader.close();
            return allLines;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public static void computeFromFile() {
        System.out.println(compute(null));
    }
}
