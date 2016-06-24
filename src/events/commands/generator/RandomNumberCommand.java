package events.commands.generator;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class RandomNumberCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        generateRandomNumber(e, args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "rnum", RunBot.PREFIX + "randomNumber", RunBot.PREFIX + "randomNum", RunBot.PREFIX + "randomnumber", RunBot.PREFIX + "rNum");
    }

    @Override
    public String getDescription() {
        return "Command that generates a random number between 0 and the argument given!";
    }

    @Override
    public String getName() {
        return "Random Number Command";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(RunBot.PREFIX + "rnum <Integer>");
    }

    private void generateRandomNumber(MessageReceivedEvent e, String[] args) {
        try {
            long rnum = (long) (Long.parseLong(args[1]) * Math.random() + 1);
            e.getChannel().sendMessage("your number is: " + rnum);
        } catch (NumberFormatException error) {
            e.getChannel().sendMessage("either your number is too big or you have not input an integer");
        }
    }
}

