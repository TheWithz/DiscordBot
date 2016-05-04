package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.*;

/**
 * Created by TheWithz on 4/24/16.
 */
public class StopCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        AudioUtil.player.stop();
        event.getChannel().sendMessage("playback has been completely stopped.");
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "stop");
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
    public java.util.List<String> getUsageInstructions() {
        return null;
    }
}

