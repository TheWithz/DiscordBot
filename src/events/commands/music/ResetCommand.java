package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;

import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class ResetCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        AudioUtil.player.stop();
        AudioUtil.player = new MusicPlayer();
        AudioUtil.player.setVolume(AudioUtil.DEFAULT_VOLUME);
        AudioUtil.manager.setSendingHandler(AudioUtil.player);
        event.getChannel().sendMessage(":white_check_mark: Music player has been completely reset.");
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "reset");
    }

    @Override
    public String getDescription() {
        return "Resets the audio player";
    }

    @Override
    public String getName() {
        return "Reset Command";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(RunBot.PREFIX + "reset");
    }
}
