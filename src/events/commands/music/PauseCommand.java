package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class PauseCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (AudioUtil.player == null) {
            event.getChannel().sendMessageAsync(":x: You cannot pause before you have started playing.", null);
            return;
        }
        if (!AudioUtil.player.isPlaying()) {
            event.getChannel().sendMessageAsync(String.format(":x: You cannot pause if %1$s has already stopped playing.", RunBot.BOT.getUsername()), null);
            return;
        }
        AudioUtil.player.pause();
        event.getChannel().sendMessage(":white_check_mark: playback has been paused.");
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "pause");
    }

    @Override
    public String getDescription() {
        return "Pauses the audio player.";
    }

    @Override
    public String getName() {
        return "Pause Command";
    }

    @Override
    public java.util.List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)\n" +
                "[Example:](%1$s) This will pause <%2$s> if there is audio playing", getAliases().get(0), RunBot.BOT.getUsername()));
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
