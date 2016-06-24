package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;

/**
 * Created by TheWithz on 4/24/16.
 */
public class JoinCommand extends Command {
    //Start an audio connection with a VoiceChannel
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        //Separates the name of the channel so that we can search for it
        String chanName = args[1];

        //Scans through the VoiceChannels in this Guild, looking for one with a case-insensitive matching name.
        VoiceChannel channel = event.getGuild().getVoiceChannels().stream().filter(
                vChan -> vChan.getName().equalsIgnoreCase(chanName))
                .findFirst().orElse(null);  //If there isn't a matching name, return null.
        if (channel == null) {
            event.getChannel().sendMessage("There isn't a VoiceChannel in this Guild with the name: '" + chanName + "'");
            return;
        }
        AudioUtil.setManagerAndPlayer(event);
        AudioUtil.manager.openAudioConnection(channel);
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "join");
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

