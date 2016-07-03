package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

import static events.commands.music.AudioUtil.player;

/**
 * Created by TheWithz on 4/24/16.
 */
public class ShuffleCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (player.isRepeat()) {
            event.getChannel().sendMessage(":x: Cannot shuffle a playlist if repeat is **on**.");
            return;
        }
        if (player.isShuffle()) {
            player.setShuffle(false);
            event.getChannel().sendMessage(":white_check_mark: The player has been set to **not** shuffle.");
        } else {
            player.setShuffle(true);
            event.getChannel().sendMessage(":white_check_mark: The player been set to shuffle.");
        }
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "shuffle");
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
    public List<String> getUsageInstructionsEveryone() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return null;
    }
}
