package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

import static events.commands.music.AudioUtil.player;

/**
 * Created by TheWithz on 4/24/16.
 */
public class RestartCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (player == null) {
            event.getChannel().sendMessageAsync(":x: Cannot restart a song if there hasn't been a song played yet.", null);
            return;
        }
        if (player.isStopped()) {
            if (player.getPreviousAudioSource() != null) {
                player.reload(true);
                event.getChannel().sendMessageAsync(":white_check_mark: The previous song has been restarted.", null);
            } else {
                event.getChannel().sendMessageAsync(":x: The player has never played a song, so it cannot restart a song.", null);
            }
        } else {
            player.reload(true);
            event.getChannel().sendMessageAsync(":white_check_mark: The currently playing song has been restarted!", null);
        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "restart");
    }

    @Override
    public String getDescription() {
        return "Restarts the current song in the audio player";
    }

    @Override
    public String getName() {
        return "Restart Command";
    }

    @Override
    public java.util.List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)]\n" +
                                                               "[Example:](%1$s) This will restart <%2$s's> currently playing song.",
                                                       getAliases().get(0),
                                                       RunBot.BOT.getUsername()));
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
