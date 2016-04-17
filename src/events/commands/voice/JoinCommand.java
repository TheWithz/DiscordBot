package events.commands.voice;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class JoinCommand extends Command {

    protected static VoiceChannel curChannel = null;

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        join(event, args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "join");
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
    public List<String> getUsageInstructions() {
        return Collections.singletonList(RunBot.prefix + "join <Voice Channel Name>");
    }

    private void join(MessageReceivedEvent event, String[] args) {
        if (curChannel != null) {
            event.getChannel().sendMessage("Cannot join *" + args[1] + "* because I am in *" + curChannel.getName() + "* already. Please use $move for existing audio connections.");
            return;
        }
        curChannel = event.getGuild().getVoiceChannels().stream().filter(
                vChan -> vChan.getName().equalsIgnoreCase(args[1]))
                .findFirst().orElse(null);  //If there isn't a matching name, return null.
        if (curChannel == null) {
            System.out.println("There isn't a VoiceChannel in this Guild with the name: '" + args[1] + "'");
            return;
        }
        event.getJDA().getAudioManager(event.getGuild()).openAudioConnection(curChannel);
    }
}
