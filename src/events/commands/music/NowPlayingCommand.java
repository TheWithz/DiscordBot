package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioTimestamp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static events.commands.music.AudioUtil.player;

/**
 * Created by TheWithz on 4/24/16.
 */
public class NowPlayingCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (player == null) {
            event.getChannel().sendMessage(":x: Cannot show information for a song that is not playing!");
            return;
        }
        if (player.isPlaying() || player.isPaused() || player.isStopped()) {
            AudioTimestamp currentTime = player.getCurrentTimestamp();
            AudioTimestamp total = player.getCurrentAudioSource().getInfo().getDuration();
            AudioInfo info = player.getCurrentAudioSource().getInfo();
            if (info.getError() == null) {
                if (player.isPlaying()) {
                    event.getChannel().sendMessage(
                            ":white_check_mark: **Playing:** " + info.getTitle() + "\n" +
                                    convert(total, currentTime, player.getVolume(), "play", player.isRepeat(), player.isShuffle()));
                } else if (player.isPaused()) {
                    event.getChannel().sendMessage(
                            ":white_check_mark: **Playing:** " + info.getTitle() + "\n" +
                                    convert(total, currentTime, player.getVolume(), "pause", player.isRepeat(), player.isShuffle()));
                }
            } else {
                if (player.isPlaying()) {
                    event.getChannel().sendMessage(
                            ":x: **Playing:** Info Error. Known source: " + player.getCurrentAudioSource().getSource() + "\n" +
                                    convert(total, currentTime, player.getVolume(), "play", player.isRepeat(), player.isShuffle()));
                } else if (player.isPaused()) {
                    event.getChannel().sendMessage(
                            ":x: **Playing:** Info Error. Known source: " + player.getCurrentAudioSource().getSource() + "\n" +
                                    convert(total, currentTime, player.getVolume(), "pause", player.isRepeat(), player.isShuffle()));
                }
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
                                                               "[Example: 2](%2$s) This will print out information about the song currently playing.", getAliases().get(0), getAliases()
                                                               .get(2)));
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return getUsageInstructionsEveryone();
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return getUsageInstructionsEveryone();
    }

    public static String convert(AudioTimestamp totalSeconds, AudioTimestamp current, float vol, String status, boolean isRepeat, boolean isShuffle) {
        String bar = " **[`" + current.getTimestamp() + "`/`" + totalSeconds.getTimestamp() + "`]** ";
        //   bar += playOrPause.equals("play") ? Symbols.PLAY.toString() : Symbols.PAUSE.toString();
        switch (status) {
            case "play":
                bar += Symbols.PLAY.toString();
                if (isRepeat) {
                    bar += Symbols.REPEAT.toString();
                } else if (isShuffle) {
                    bar += Symbols.SHUFFLE.toString();
                }
                break;
            case "pause":
                bar += Symbols.PAUSE.toString();
                break;
        }
        int percentage = (int) (((double) current.getTotalSeconds() / totalSeconds.getTotalSeconds()) * 10);
        int i;
        for (i = 0; i < percentage; i++) {
            bar += Symbols.SPACE;
        }

        bar += Symbols.CURRENT;

        while (i < 10) {
            bar += Symbols.SPACE;
            i++;
        }

        bar += (vol > .25f) ? vol > .5f ? Symbols.LOUD_VOLUME : Symbols.MED_VOLUME : vol < .1f ? Symbols.MUTE : Symbols.LOW_VOLUME;

        return bar;
    }
}
