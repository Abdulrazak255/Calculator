package RekeningMachine;
import java.util.Stack;

public class RekeningMachine {
    public static void main(String[] args) {
        String equation = "(4+3)-2*5+0.3/3";
        try {
            double result = evaluateExpression(equation);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Error in expression: " + e.getMessage());
        }
    }

    public static double evaluateExpression(String expression) {
        String postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    private static String infixToPostfix(String expression) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operators = new Stack<>();

        for (char token : expression.toCharArray()) {
            if (token == ' ') {
                continue;
            }
            if (Character.isDigit(token) || token == '.') {
                postfix.append(token);
            } else if (token == '(') {
                operators.push(token);
            } else if (token == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    postfix.append(" ").append(operators.pop());
                }
                operators.pop(); // Remove '('
            } else { // Operator
                postfix.append(" ");
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    postfix.append(operators.pop()).append(" ");
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            postfix.append(" ").append(operators.pop());
        }
        return postfix.toString().trim();
    }

    private static double evaluatePostfix(String postfix) {
        Stack<Double> operands = new Stack<>();
        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            if (Character.isDigit(token.charAt(0)) || token.length() > 1 && Character.isDigit(token.charAt(1))) {
                operands.push(Double.parseDouble(token));
            } else {
                double operand2 = operands.pop();
                double operand1 = operands.pop();
                switch (token) {
                    case "+":
                        operands.push(operand1 + operand2);
                        break;
                    case "-":
                        operands.push(operand1 - operand2);
                        break;
                    case "*":
                        operands.push(operand1 * operand2);
                        break;
                    case "/":
                        operands.push(operand1 / operand2);
                        break;
                }
            }
        }
        return operands.pop();
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

}