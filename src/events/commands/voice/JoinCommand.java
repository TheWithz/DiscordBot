package events.commands.voice;

import events.commands.Command;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class JoinCommand extends Command {

    VoiceChannel curChannel = null;

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        curChannel = event.getGuild().getVoiceChannels().stream().filter(
                vChan -> vChan.getName().equalsIgnoreCase(args[1]))
                .findFirst().orElse(null);  //If there isn't a matching name, return null.
        if (curChannel == null) {
            System.out.println("There isn't a VoiceChannel in this Guild with the name: '" + args[1] + "'");
            return;
        }
        event.getJDA().getAudioManager().openAudioConnection(curChannel);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("$join");
    }

    @Override
    public String getDescription() {
        return "Command that joins a voice channel!";
    }

    @Override
    public String getName() {
        return "Join Command";
    }

    @Override
    public String getUsageInstructions() {
        return "$join <Voice Channel Name>";
    }
}