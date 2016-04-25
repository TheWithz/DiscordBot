package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioTimestamp;

import java.util.Arrays;

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
                        "**Playing:** " + info.getTitle() + "\n" +
                                "**Time:**    [" + currentTime.getTimestamp() + " / " + info.getDuration().getTimestamp() + "]");
            } else {
                event.getChannel().sendMessage(
                        "**Playing:** Info Error. Known source: " + AudioUtil.player.getCurrentAudioSource().getSource() + "\n" +
                                "**Time:**    [" + currentTime.getTimestamp() + " / (N/A)]");
            }
        } else {
            event.getChannel().sendMessage("The player is not currently playing anything!");
        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "nowplaying", RunBot.prefix + "currentsong", RunBot.prefix + "current");
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
