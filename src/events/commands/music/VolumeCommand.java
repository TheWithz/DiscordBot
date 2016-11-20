package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class VolumeCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 1, ":x: No volume was specified to change to. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        if(args[1].equals("-infinity") || args[1].equals("infinity") || args[1].equals("NaN")){
            event.getChannel().sendMessage(":x: How did you know this command took a float? Anyway... please just use a number between 0 and 1.0").queue();
            return;
        }

//        if (AudioUtil.player == null) {
//            event.getChannel().sendMessage(":x: Cannot change volume of player at this time").queue();
//            return;
//        }
//
//        float volume = Float.parseFloat(args[1]);
//        volume = Math.min(1F, Math.max(0F, volume));
//        AudioUtil.player.setVolume(volume);
//        event.getChannel().sendMessage(":white_check_mark: volume was changed to: " + volume).queue();
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
        return Collections.singletonList(
                String.format("(%1$s) <volume>\n" +
                                      "[Example:](%1$s) <0.5> This will set <%2$s>'s audio player to 50 Percent.", getAliases().get(0), RunBot.BOT.getName()));
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
