package events.commands.voice;

import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class MoveCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        event.getJDA().getAudioManager().moveAudioConnection(event.getGuild().getVoiceChannels().stream().filter(
                vChan -> vChan.getName().equalsIgnoreCase(args[1]))
                .findFirst().orElse(null));
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("$move");
    }

    @Override
    public String getDescription() {
        return "Command That moves bot to a different voice channel!";
    }

    @Override
    public String getName() {
        return "Move Command";
    }

    @Override
    public String getUsageInstructions() {
        return "$move <Voice Channel Name>";
    }
}
