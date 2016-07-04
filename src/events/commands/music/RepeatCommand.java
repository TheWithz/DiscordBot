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
        if (player == null) {
            sendMessage(event, ":x: Cannot repeat a song if there is no song playing.");
            return;
        }
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
        return "Sets the current song to repeat";
    }

    @Override
    public String getName() {
        return "Repeat Command";
    }

    @Override
    public java.util.List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)]\n" +
                                                               "[Example:](%1$s) This will set the current song <%2$s> is playing, to repeat. Running the command again will turn" +
                                                               " repeat off.",
                                                       getAliases().get(0),
                                                       RunBot.BOT.getUsername()));
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return getUsageInstructionsEveryone();
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return getUsageInstructionsEveryone();
    }
}

