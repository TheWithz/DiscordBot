package events.commands.generator;

import calculator.EvalPostfix;
import calculator.InfixToPostfix;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/15/16.
 */
public class CalculatorCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        calculate(args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("$calc", "$calculate");
    }

    @Override
    public String getDescription() {
        return "Command that calculates an expression!";
    }

    @Override
    public String getName() {
        return "Calculate Command";
    }

    @Override
    public String getUsageInstructions() {
        return "$calc <Mathematical Expression>";
    }

    private void calculate(String[] commandArguments) {
        StringBuilder inFixExpression = new StringBuilder();
        for (int i = 1; i < commandArguments.length; i++) {
            inFixExpression.append(commandArguments[i]);
            inFixExpression.append(" ");
        }
        System.out.println("The answer to your expression is: "
                + EvalPostfix.evalPostFix(InfixToPostfix.convertToPostfix(inFixExpression.toString())));
    }

}
