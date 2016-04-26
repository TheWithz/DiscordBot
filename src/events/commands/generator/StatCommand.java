package events.commands.generator;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class StatCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        event.getChannel().sendMessage(RunBot.getUptime());
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "stats");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getUsageInstructions() {
        return null;
    }
}
