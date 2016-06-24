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
        if(AudioUtil.player == null){
            event.getChannel().sendMessage("Cannot change volume of player at this time");
            return;
        }
        float volume = Float.parseFloat(args[1]);
        volume = Math.min(1F, Math.max(0F, volume));
        AudioUtil.player.setVolume(volume);
        event.getChannel().sendMessage("volume was changed to: " + volume);
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "volume");
    }

    @Override
    public String getDescription() {
        return "Changes the volume of the audio player";
    }

    @Override
    public String getName() {
        return "Volume Command";
    }

    @Override
    public java.util.List<String> getUsageInstructions() {
        return Arrays.asList(RunBot.PREFIX + "volume");
    }
}
