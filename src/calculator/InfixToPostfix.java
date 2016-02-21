package calculator;

import java.util.*;

public class InfixToPostfix {

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '(' || c == ')';
    }

    private static boolean isSpace(char c) {
        return (c == ' ');
    }

    private static boolean lowerPrecedence(char op1, char op2) {
        // Tell whether op1 has lower precedence than op2, where op1 is an
        // operator on the left and op2 is an operator on the right.
        // op1 and op2 are assumed to be operator characters (+,-,*,/,^).
        switch (op1) {
            case '+':
            case '-':
                return !(op2 == '+' || op2 == '-');
            case '*':
            case '/':
                return op2 == '^' || op2 == '(';
            case '^':
                return op2 == '(';
            case '(':
                return true;
            default:
                return false;
        }

    }

    public static String convertToPostfix(String infix) {
        Stack<String> operatorStack = new Stack<String>();
        char c;
        StringTokenizer parser = new StringTokenizer(infix, "+-*/^() ", true);
        StringBuffer postfix = new StringBuffer(infix.length());

        while (parser.hasMoreTokens()) {
            String token = parser.nextToken();
            c = token.charAt(0);
            if ((token.length() == 1) && isOperator(c)) {
                while (!operatorStack.empty() && !lowerPrecedence(operatorStack.peek().charAt(0), c))
                    postfix.append(" ").append(operatorStack.pop());
                if (c == ')') {
                    String operator = operatorStack.pop();
                    while (operator.charAt(0) != '(') {
                        postfix.append(" ").append(operator);
                        operator = operatorStack.pop();
                    }
                } else
                    operatorStack.push(token);
            } else if ((token.length() == 1) && isSpace(c)) {
            } else {
                postfix.append(" ").append(token);
            }
        }

        while (!operatorStack.empty())
            postfix.append(" ").append(operatorStack.pop());
        return postfix.toString();

    }

    public static void main(String[] args) {
        System.out.print("hi");
    }

}