package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collections;

/**
 * Created by TheWithz on 4/24/16.
 */
public class RepeatCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (AudioUtil.player.isRepeat()) {
            AudioUtil.player.setRepeat(false);
            event.getChannel().sendMessage(":white_check_mark: The player has been set to **not** repeat.");
        } else {
            AudioUtil.player.setRepeat(true);
            event.getChannel().sendMessage(":white_check_mark: The player been set to repeat.");
        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "repeat");
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

