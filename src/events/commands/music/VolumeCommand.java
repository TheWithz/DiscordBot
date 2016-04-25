package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;

/**
 * Created by TheWithz on 4/24/16.
 */
public class VolumeCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        float volume = Float.parseFloat(args[1]);
        volume = Math.min(1F, Math.max(0F, volume));
        AudioUtil.player.setVolume(volume);
        event.getChannel().sendMessage("volume was changed to: " + volume);
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "volume");
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
