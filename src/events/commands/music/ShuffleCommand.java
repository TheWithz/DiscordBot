package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

import static sun.audio.AudioPlayer.player;

/**
 * Created by TheWithz on 4/24/16.
 */
public class ShuffleCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (player == null) {
            event.getChannel().sendMessage(":x: Cannot shuffle a song if there is no song playing.").queue();
            return;
        }
//        if (player.isRepeat()) {
//            event.getChannel().sendMessage(":x: Cannot shuffle a playlist if repeat is **on**.").queue();
//            return;
//        }
//        if (player.isShuffle()) {
//            player.setShuffle(false);
//            event.getChannel().sendMessage(":white_check_mark: The player has been set to **not** shuffle.").queue();
//        } else {
//            player.setShuffle(true);
//            event.getChannel().sendMessage(":white_check_mark: The player been set to shuffle.").queue();
//        }
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "shuffle");
    }

    @Override
    public String getDescription() {
        return "Shuffles the current playlist of songs";
    }

    @Override
    public String getName() {
        return "Shuffle Command";
    }

    @Override
    public List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)]\n" +
                                                               "[Example:](%1$s) This will set <%2$s's> current playlist to shuffle. Running the command again will turn shuffle " +
                                                               "off. ",
                                                       getAliases().get(0),
                                                       RunBot.BOT.getName()));
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
