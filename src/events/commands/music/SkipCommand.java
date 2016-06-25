package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.*;

/**
 * Created by TheWithz on 4/24/16.
 */
public class SkipCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        AudioUtil.player.skipToNext();
        event.getChannel().sendMessage(":white_check_mark: Skipped the current song.");
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "skip");
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public java.util.List<String> getUsageInstructionsEveryone() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return null;
    }
}
