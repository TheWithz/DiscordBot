package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class RestartCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (AudioUtil.player.isStopped()) {
            if (AudioUtil.player.getPreviousAudioSource() != null) {
                AudioUtil.player.reload(true);
                event.getChannel().sendMessage(":white_check_mark: The previous song has been restarted.");
            } else {
                event.getChannel().sendMessage(":x: The player has never played a song, so it cannot restart a song.");
            }
        } else {
            AudioUtil.player.reload(true);
            event.getChannel().sendMessage(":white_check_mark: The currently playing song has been restarted!");
        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "restart");
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
