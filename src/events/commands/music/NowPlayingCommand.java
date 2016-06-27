package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioTimestamp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class NowPlayingCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (AudioUtil.player.isPlaying()) {
            AudioTimestamp currentTime = AudioUtil.player.getCurrentTimestamp();
            AudioInfo info = AudioUtil.player.getCurrentAudioSource().getInfo();
            if (info.getError() == null) {
                event.getChannel().sendMessage(
                        ":white_check_mark: **Playing:** " + info.getTitle() + "\n" +
                                "**Time:**    [" + currentTime.getTimestamp() + " / " + info.getDuration().getTimestamp() + "]");
            } else {
                event.getChannel().sendMessage(
                        ":white_check_mark: **Playing:** Info Error. Known source: " + AudioUtil.player.getCurrentAudioSource().getSource() + "\n" +
                                "**Time:**    [" + currentTime.getTimestamp() + " / (N/A)]");
            }
        } else {
            event.getChannel().sendMessage(":x: The player is not currently playing anything!");
        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "nowplaying", RunBot.PREFIX + "currentsong", RunBot.PREFIX + "current");
    }

    @Override
    public String getDescription() {
        return "Prints out information about the currently playing song";
    }

    @Override
    public String getName() {
        return "NowPlaying Command";
    }

    @Override
    public java.util.List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)\n" +
                "[Example: 1](%1$s) This will print out information about the song currently playing.\n" +
                "[Example: 2](%2$s) This will print out information about the song currently playing.", getAliases().get(0), getAliases().get(2)));
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
