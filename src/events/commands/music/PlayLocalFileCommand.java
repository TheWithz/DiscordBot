package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/17/16.
 */
public class PlayLocalFileCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        play(e, args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "play");
    }

    @Override
    public String getDescription() {
        return "Command that plays music!";
    }

    @Override
    public String getName() {
        return "Play Command";
    }

    @Override
    public List<String> getUsageInstructions() {
        // TODO: 2/21/16 explain implementations of play
        return Collections.singletonList("");
    }

    private void play(MessageReceivedEvent event, String[] args) {
        RunBot.API.getAudioManager(event.getGuild()).getSendingHandler().provide20MsAudio();
    }
}
