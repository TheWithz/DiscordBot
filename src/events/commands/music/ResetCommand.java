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
public class ResetCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (player == null) {
            event.getChannel().sendMessage(":x: Cannot reset the player if it hasn't been created yet!").queue();
            return;
        }
        player.stop();
//        player = new MusicPlayer();
//        player.setVolume(AudioUtil.DEFAULT_VOLUME);
//        AudioUtil.manager.setSendingHandler(player);
        event.getChannel().sendMessage(":white_check_mark: Music player has been completely reset.").queue();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "reset");
    }

    @Override
    public String getDescription() {
        return "Resets the audio player";
    }

    @Override
    public String getName() {
        return "Reset Command";
    }

    @Override
    public List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)]\n" +
                                                               "[Example:](%1$s) This will reset <%2$s's> audio player.", getAliases().get(0), RunBot.BOT.getName()));
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
