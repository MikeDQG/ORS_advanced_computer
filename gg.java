import java.util.Scanner;

public class gg {
	  public static void main(String[] args) {
		    Scanner scanner = new Scanner(System.in);

		    // Read in a mathematical expression
		    System.out.print("Enter a mathematical expression: ");
		    String expression = scanner.nextLine();

		    // Parse the expression and evaluate it
		    int result = evaluate(expression);

		    // Print the result
		    System.out.println("Result: " + result);

		    // Close the scanner
		    scanner.close();
		  }


  public static int evaluate(String expression) {
    // Base case: If the expression is a single number, return its value
    if (expression.length() == 1 && Character.isDigit(expression.charAt(0))) {
      return Integer.parseInt(expression);
    }

    // Iterate through the expression character by character
    int result = 0;
    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);

      // If the character is a left parenthesis, find the matching right parenthesis and evaluate the expression within the parentheses
      if (c == '(') {
        int parenCount = 1;
        int j = i + 1;
        while (parenCount > 0) {
          if (expression.charAt(j) == '(') {
            parenCount++;
          } else if (expression.charAt(j) == ')') {
            parenCount--;
          }
          j++;
        }
        int subexpression = evaluate(expression.substring(i+1, j-1));

        // If the character before the left parenthesis is an operator, apply it to the result
        if (i > 0 && (expression.charAt(i-1) == '+' || expression.charAt(i-1) == '-' || expression.charAt(i-1) == '*' || expression.charAt(i-1) == '/')) {
          result = applyOperation(expression.charAt(i-1), result, subexpression);
        } else {
          result = subexpression;
        }
        i = j;
      }
      // If the character is an operator, store it and wait for the next value
      else if (c == '+' || c == '-' || c == '*' || c == '/') {
        char operator = c;
        int j = i + 1;
        while (j < expression.length() && !Character.isDigit(expression.charAt(j))) {
          j++;
        }
        int subexpression = evaluate(expression.substring(i+1, j));
        result = applyOperation(operator, result, subexpression);
        i = j - 1;
      }
    }

    // Return the result
    return result;
  }

  public static int applyOperation(char operator, int b, int a) {
    switch (operator) {
      case '+':
        return a + b;
      case '-':
        return a - b;
      case '*':
        return a * b;
      case '/':
        return a / b;
    }
    System.out.println("jebi se");
    return 0;
  }}
