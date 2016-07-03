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
public class RepeatCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (player.isShuffle()) {
            event.getChannel().sendMessage(":x: Cannot repeat a song if shuffle is **on**.");
            return;
        }
        if (player.isRepeat()) {
            player.setRepeat(false);
            event.getChannel().sendMessage(":white_check_mark: The player has been set to **not** repeat.");
        } else {
            player.setRepeat(true);
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
    public java.util.List<String> getUsageInstructionsEveryone() {
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

