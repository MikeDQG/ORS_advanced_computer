import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

public class stevilskiSistemi {

    public static void main(String[] args) {
        
    System.out.println(input(null));
        // System.out.println(input(null));    // if you expect to type commands in terminal, insert "null"
    }

    private static void testInput(String input, String expectedOutput) {
        assert input(input).equals(expectedOutput);
    }

    public static String input(String testInput) {
        Scanner inputScanner = new Scanner(System.in);
        String input = new String();
        if (testInput == null) {
            input = inputScanner.nextLine();
        } else {
            input = testInput;
        }
        StringBuilder strBldInput = new StringBuilder();
        strBldInput.append(input);
        strBldInput.append(" ");
        String[] commands = input.split(" ");
        if (commands[1].equals("0")) {
            strBldInput.append("0");
            return strBldInput.toString();
        }
        String[] possibleComm = { "DEC", "BIN", "OCT", "HEX" };
        int[] verifiedValues = new int[4];
        if (commands[0].equals(possibleComm[0])) {
            if (commands[2].equals(possibleComm[1])) {
                String bin = decimalToCustom(Integer.parseInt(commands[1]), 2);
                strBldInput.append(bin);
                // System.out.println(bin);
            } else if (commands[2].equals(possibleComm[2])) {
                String oct = decimalToCustom(Integer.parseInt(commands[1]), 8);
                strBldInput.append(oct);
                // System.out.println(oct);
            } else if (commands[2].equals(possibleComm[3])) {
                String hex = decimalToCustom(Integer.parseInt(commands[1]), 16);
                strBldInput.append(hex);
                // System.out.println(hex);
            } else {
                throw new IllegalArgumentException("Invalid numerical type: " + commands[2]);
            }
        } else if (commands[0].equals(possibleComm[1])) {
            if (commands[2].equals(possibleComm[0])) {
                String dec = Integer.toString(binaryToDecimal(commands[1]));
                strBldInput.append(dec);
                // System.out.println(dec);
            } else if (commands[2].equals(possibleComm[2])) {
                String oct = binaryToOct(commands[1]);
                strBldInput.append(oct);
                // System.out.println(oct);
            } else if (commands[2].equals(possibleComm[3])) {
                String hex = binaryToHex(commands[1]);
                strBldInput.append(hex);
                // System.out.println(hex);
            } else {
                throw new IllegalArgumentException("Invalid numerical type: " + commands[2]);
            }
        } else if (commands[0].equals(possibleComm[2])) {
            if (commands[2].equals(possibleComm[0])) {
                String dec = Integer.toString(octToDecimal(commands[1]));
                strBldInput.append(dec);
                // System.out.println(dec);
            } else if (commands[2].equals(possibleComm[1])) {
                String bin = octToBinary(commands[1]);
                strBldInput.append(bin);
                // System.out.println(bin);
            } else if (commands[2].equals(possibleComm[3])) {
                String hex = octToHex(commands[1]);
                strBldInput.append(hex);
                // System.out.println(hex);
            } else {
                throw new IllegalArgumentException("Invalid numerical type: " + commands[2]);
            }
        } else if (commands[0].equals(possibleComm[3])) {
            if (commands[2].equals(possibleComm[0])) {
                String dec = Integer.toString(hexToDecimal(commands[1]));
                strBldInput.append(dec);
                // System.out.println(dec);
            } else if (commands[2].equals(possibleComm[1])) {
                String bin = hexToBinary(commands[1]);
                strBldInput.append(bin);
                // System.out.println(bin);
            } else if (commands[2].equals(possibleComm[2])) {
                String oct = hexToOct(commands[1]);
                strBldInput.append(oct);
                // System.out.println(oct);
            } else {
                throw new IllegalArgumentException("Invalid numerical type: " + commands[2]);
            }
        } else {
            throw new IllegalArgumentException("Invalid command " + commands[0]);
        }

        return strBldInput.toString();
    }

    private static String hexToOct(String string) {
        return decimalToCustom(hexToDecimal(string), 8);
    }

    private static String hexToBinary(String string) {
        return decimalToCustom(hexToDecimal(string), 2);
    }

    private static String octToHex(String string) {
        return decimalToCustom(octToDecimal(string), 16);
    }

    private static String octToBinary(String string) {
        return decimalToCustom(octToDecimal(string), 2);
    }

    private static String binaryToHex(String string) {
        return decimalToCustom(binaryToDecimal(string), 16);
    }

    private static String binaryToOct(String string) {
        return decimalToCustom(binaryToDecimal(string), 8);
    }

    public static int binaryToDecimal(String binary) {
        int decimal = 0;
        for (int i = 0; i < binary.length(); i++) {
            char c = binary.charAt(binary.length() - i - 1);
            if (c == '1') {
                decimal += Math.pow(2, i);
            } else if (c != '0') {
                throw new IllegalArgumentException("Invalid binary string: " + binary);
            }
        }
        return decimal;
    }

    public static int octToDecimal(String octal) {
        int decimal = 0;
        for (int i = 0; i < octal.length(); i++) {
            char c = octal.charAt(i);
            decimal = decimal * 8 + octCharToDecimal(c);
        }
        return decimal;
    }

    public static int octCharToDecimal(char c) {
        if (c >= '0' && c <= '7') {
            return c - '0';
        } else {
            throw new IllegalArgumentException("Invalid octal character: " + c);
        }
    }

    public static int hexToDecimal(String hex) {
        int decimal = 0;
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            decimal = decimal * 16 + charToDecimal(c);
        }
        return decimal;
    }

    public static int charToDecimal(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'A' && c <= 'F') {
            return 10 + c - 'A';
        } else {
            throw new IllegalArgumentException("Invalid hexadecimal character: " + c);
        }
    }

    private static String decimalToCustom(int decimal, int step) {
        StringBuilder numerical = new StringBuilder();
        while (decimal > 0) {
            int digit = decimal % step;
            if (digit < 10) {
                numerical.insert(0, digit);
            } else {
                numerical.insert(0, (char) ('A' + digit - 10));
            }
            decimal /= step;
        }
        return numerical.toString();
    }

    private static ArrayList<String> readLineFromFile() {
        ArrayList<String> allLines = new ArrayList<>();
        try {
            File myObj = new File("stevilski_sistemi.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                allLines.add(data);
                // System.out.println(data);
            }
            myReader.close();
            return allLines;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public static String convertFromFile() {
        ArrayList<String> lines = readLineFromFile();
        for (int i = 0; i < lines.size(); i++) {
            System.out.println(input(lines.get(i)));
        }
        return null;
    }
}