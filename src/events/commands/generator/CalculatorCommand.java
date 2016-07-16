package events.commands.generator;

import bots.RunBot;
import calculator.EvalPostfix;
import calculator.InfixToPostfix;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/15/16.
 */
public class CalculatorCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessageAsync(calculate(args), null);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "calc", RunBot.PREFIX + "calculate");
    }

    @Override
    public String getDescription() {
        return "Calculates a given expression.";
    }

    @Override
    public String getName() {
        return "Calculate Command";
    }

    @Override
    public List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s) <Mathematical Expression>\n" +
                                                               "[Example:](%1$s) <4 + 4 + 4 * 6> This will output 32.", getAliases().get(0)));
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return getUsageInstructionsEveryone();
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return getUsageInstructionsEveryone();
    }

    private String calculate(String[] commandArguments) {
        StringBuilder inFixExpression = new StringBuilder();
        for (int i = 1; i < commandArguments.length; i++) {
            inFixExpression.append(commandArguments[i]);
            inFixExpression.append(" ");
        }
        return ":white_check_mark: The answer to your expression is: "
                + EvalPostfix.evalPostFix(InfixToPostfix.convertToPostfix(inFixExpression.toString()));
    }

}
