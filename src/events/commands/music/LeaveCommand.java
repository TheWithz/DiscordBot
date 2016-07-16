package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class LeaveCommand extends Command {
    //Disconnect the audio connection with the VoiceChannel.
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (AudioUtil.manager.isConnected()) {
            AudioUtil.player.stop();
            AudioUtil.manager.closeAudioConnection();
            AudioUtil.manager = null;
            AudioUtil.player = null;
        } else
            event.getChannel().sendMessageAsync(":x: Cannot sever null audio connection.", null);
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "leave");
    }

    @Override
    public String getDescription() {
        return "Leaves the voice channel that " + RunBot.BOT.getUsername() + " is connected to.";
    }

    @Override
    public String getName() {
        return "Leave Command";
    }

    @Override
    public java.util.List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)\n" +
                                                               "[Example:](%1$s) This will automatically remove <%2$s> from it's current audio channel.", getAliases().get(0), RunBot.BOT
                                                               .getUsername()));
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

