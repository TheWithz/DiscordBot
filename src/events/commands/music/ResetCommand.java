package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;

import java.util.Arrays;
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
        event.getChannel().sendMessage("Music player has been completely reset.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "reset");
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
