package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class ShuffleCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (AudioUtil.player.isShuffle()) {
            AudioUtil.player.setShuffle(false);
            event.getChannel().sendMessage("The player has been set to **not** shuffle.");
        } else {
            AudioUtil.player.setShuffle(true);
            event.getChannel().sendMessage("The player been set to shuffle.");
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "shuffle");
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
    public List<String> getUsageInstructions() {
        return null;
    }
}
