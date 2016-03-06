package events.commands.voice;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class LeaveCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        event.getJDA().getAudioManager().closeAudioConnection();
        JoinCommand.curChannel = null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "leave");
    }

    @Override
    public String getDescription() {
        return "Command that leaves a voice channel!";
    }

    @Override
    public String getName() {
        return "Leave Command";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(RunBot.prefix + "leave");
    }
}
