package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;

/**
 * Created by TheWithz on 4/24/16.
 */
public class RestartCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (AudioUtil.player.isStopped()) {
            if (AudioUtil.player.getPreviousAudioSource() != null) {
                AudioUtil.player.reload(true);
                event.getChannel().sendMessage("The previous song has been restarted.");
            } else {
                event.getChannel().sendMessage("The player has never played a song, so it cannot restart a song.");
            }
        } else {
            AudioUtil.player.reload(true);
            event.getChannel().sendMessage("The currently playing song has been restarted!");
        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "restart");
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
