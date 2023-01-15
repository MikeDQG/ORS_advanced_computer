import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.lang.model.element.Element;
import javax.sound.sampled.Line;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class computer {

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
        System.out.println(compute(null));
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
        int[] numbers = new int[equation.size()];
        String[] operators = new String[equation.size()];
        String[] possibleOperators = { "+", "-", "*", "/", "(", ")", "=", "pow", "sqrt", "," };
        int[] isDone = new int[equation.size()];
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
        double answer = 0;
        EqElement eqElement = new EqElement();
        eqElement.end = equation.size() - 1;
        ArrayList<EqElement> collectionOfEqElements = new ArrayList<EqElement>();
        collectionOfEqElements.add(eqElement);
        answer = getValue(equation, eqElement, numbers, operators, possibleOperators, isDone, collectionOfEqElements);
        return String.valueOf(answer);
    }

    private static double getValue(ArrayList<String> equation, EqElement eqElement, int[] numbers, String[] operators,
            String[] possibleOperators, int[] isDone, ArrayList<computer.EqElement> collectionOfEqElements) {
        String[] priorities = new String[] { "pow", "sqrt", "(", "/", "*", "-", "+" };
        double answer = 0;
        int parenthesisCounter = 0;
        EqElement newElEqElement = new EqElement();
        switch (equation.get(eqElement.start)) {
            case "pow":
                parenthesisCounter = 0;
                int powComa = -1;
                for (int i = eqElement.start + 1; i < operators.length; i++) {
                    System.out.println(operators[i]);
                    if (operators[i] != null) {
                        if (operators[i].equals(")")) {
                            if (parenthesisCounter <= 1) {
                                newElEqElement.start = eqElement.start + 1;
                                newElEqElement.end = i - 1;
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
                first.initialiseElement(newElEqElement.start, powComa - 1);
                second.initialiseElement(powComa + 1, newElEqElement.end);
                answer = Math.pow(
                        getValue(equation, first, numbers, operators, possibleOperators, isDone,
                                collectionOfEqElements),
                        getValue(equation, second, numbers, operators, possibleOperators, isDone,
                                collectionOfEqElements));
                break;
            case "(":
                parenthesisCounter = 1;
                for (int i = eqElement.start + 1; i < operators.length; i++) {
                    System.out.println(operators[i]);
                    if (operators[i] != null) {
                        if (operators[i].equals(")")) {
                            if (parenthesisCounter <= 1) {
                                newElEqElement.start = eqElement.start + 1;
                                newElEqElement.end = i - 1;
                                newElEqElement.setNewElement(equation, newElEqElement);
                                isDone[newElEqElement.start] = 1;
                                isDone[newElEqElement.end] = 1;
                                System.out.println("makin a new element");
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
                        collectionOfEqElements);
                break;
            case "+":
                //
            default:
                assert equation.get(eqElement.start) != null;
                answer = (double) Integer.valueOf(equation.get(eqElement.start));
                break;
        }
        EqElement anothEqElement = new EqElement();
        int tempInt = newElEqElement.start;
        anothEqElement.start = tempInt;
        anothEqElement.start -= 1;
        tempInt = newElEqElement.end;
        anothEqElement.end = tempInt;
        anothEqElement.start += 1;
        isDone = makeDone(isDone, anothEqElement);
        anothEqElement.start = newElEqElement.end + 1;
        anothEqElement.end = eqElement.end;
        int notDone = checkIfDone(isDone, eqElement);
        if (notDone != -1) {
            anothEqElement.start = notDone;
        }
        return answer;
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
}
