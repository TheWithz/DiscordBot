package events;

import calculator.EvalPostfix;
import calculator.InfixToPostfix;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * Created by TheWithz on 2/15/16.
 */
public class CalculatorHandler extends ListenerAdapter {

    public CalculatorHandler() {

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith("$calc "))
            calculate(event.getMessage().getContent().split("\\s+"));

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
