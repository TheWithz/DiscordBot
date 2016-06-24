package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collections;

/**
 * Created by TheWithz on 4/24/16.
 */
public class PauseCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        AudioUtil.player.pause();
        event.getChannel().sendMessage("playback has been paused.");
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "pause");
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
