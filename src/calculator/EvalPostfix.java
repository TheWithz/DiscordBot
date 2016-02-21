package calculator;

import java.util.Stack;

public class EvalPostfix {

	public static Double evalPostFix(String x) {
		x.trim();
		String[] arr = x.split("\\s+");
		Stack<String> stack = new Stack<String>();
		Double second;
		Double first;
		for (String k : arr) {
			switch (k) {
			case "+":
				stack.push(Double.parseDouble(stack.pop()) + Double.parseDouble(stack.pop()) + "");
				break;
			case "-":
				second = Double.parseDouble(stack.pop());
				first = Double.parseDouble(stack.pop());
				stack.push(first - second + "");
				break;
			case "*":
				stack.push(Double.parseDouble(stack.pop()) * Double.parseDouble(stack.pop()) + "");
				break;
			case "/":
				second = Double.parseDouble(stack.pop());
				first = Double.parseDouble(stack.pop());
				stack.push(first / second + "");
				break;
			case "^":
				second = Double.parseDouble(stack.pop());
				first = Double.parseDouble(stack.pop());
				stack.push(Math.pow(first, second) + "");
				break;
			default:
				stack.push(k);
				break;
			}
		}
		return Double.parseDouble(stack.pop());
	}
}
