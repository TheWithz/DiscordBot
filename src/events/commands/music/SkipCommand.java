package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class SkipCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        //   AudioUtil.player.skipToNext();
        event.getChannel().sendMessage(":white_check_mark: Skipped the current song.").queue();
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "skip");
    }

    @Override
    public String getDescription() {
        return "Skips the currently playing song and moves to the next one in the queue.";
    }

    @Override
    public String getName() {
        return "Skip Command";
    }

    @Override
    public java.util.List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)]\n" +
                                                               "[Example:](%1$s) This will skip the song <%2$s> is currently playing and then move to the next song in the queue",
                                                       getAliases().get(0),
                                                       RunBot.BOT.getName()));
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
