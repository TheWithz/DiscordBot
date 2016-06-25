package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class VolumeCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 1, ":x: No volume was specified to change to. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        if (AudioUtil.player == null) {
            event.getChannel().sendMessage(":x: Cannot change volume of player at this time");
            return;
        }

        float volume = Float.parseFloat(args[1]);
        volume = Math.min(1F, Math.max(0F, volume));
        AudioUtil.player.setVolume(volume);
        event.getChannel().sendMessage(":white_check_mark: volume was changed to: " + volume);
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "volume");
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
    public java.util.List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(RunBot.PREFIX + "volume");
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
