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
public class StatCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        System.out.println("add pulling from database stuff");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "stats", RunBot.PREFIX + "uptime");
    }

    @Override
    public String getDescription() {
        return "Prints statistics";
    }

    @Override
    public String getName() {
        return "Stat Command";
    }

    @Override
    public List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)]\n" +
                                                               "[Example:](%1$s) This will print <%2$s's> statistics",
                                                       getAliases().get(0), RunBot.BOT.getUsername()));
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return getUsageInstructionsEveryone();
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return getUsageInstructionsEveryone();
    }
}
