package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.Playlist;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class PlayCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (args.length == 1) {
            handlePlay(event);
            return;
        }

        switch (args[1]) {
            case "url":
                handleUrl(event, args);
                break;
            case "playlist":
                handlePlaylist(event, args);
                break;
            default:
                sendMessage(event, ":x: Unknown Action argument: `" + args[1] + "` was provided. " +
                        "Please use `" + RunBot.PREFIX + "help " + getAliases().get(0) + "` for more information.");
                break;
        }

    }

    private void handlePlay(MessageReceivedEvent event) {
        if (AudioUtil.player.isPlaying()) {
            event.getChannel().sendMessage(":x: player is already playing!");
        } else if (AudioUtil.player.isPaused()) {
            AudioUtil.player.play();
            event.getChannel().sendMessage(":white_check_mark: playback as been resumed.");
        } else {
            if (AudioUtil.player.getAudioQueue().isEmpty())
                event.getChannel()
                     .sendMessage(":x: The current audio queue is empty! Add something to the queue first!");
            else {
                AudioUtil.player.play();
                event.getChannel().sendMessage(":white_check_mark: player has started playing!");
            }
        }
    }

    private void handlePlaylist(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No local playlist was provided to play from. See " + RunBot.PREFIX + "help " + getAliases().get(0));

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Playlists.json"))));
                    event.getChannel().sendMessage(RunBot.PREFIX + "play " + obj.getString(args[2]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void handleUrl(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No URL was provided to play from. See " + RunBot.PREFIX + "help " + getAliases().get(0));

        String msg = "";
        String url = args[1];
        Playlist playlist = Playlist.getPlaylist(url);
        List<AudioSource> sources = new LinkedList(playlist.getSources());
//                AudioSource source = new RemoteSource(url);
//                AudioSource source = new LocalSource(new File(url));
//                AudioInfo info = source.getInfo();   //Preload the audio info.
        if (sources.size() > 1) {
            event.getChannel().sendMessage(":white_check_mark: Found a playlist with **" + sources.size() + "** entries.\n" +
                                                   "Proceeding to gather information and queue sources. This may take some time...");
            final MusicPlayer fPlayer = AudioUtil.player;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    for (Iterator<AudioSource> it = sources.iterator(); it.hasNext(); ) {
                        AudioSource source = it.next();
                        AudioInfo info = source.getInfo();
                        List<AudioSource> queue = fPlayer.getAudioQueue();
                        if (info.getError() == null) {
                            queue.add(source);
                            if (fPlayer.isStopped())
                                fPlayer.play();
                        } else {
                            event.getChannel()
                                 .sendMessage(":x: Error detected, skipping source. Error:\n" + info.getError());
                            it.remove();
                        }
                    }
                    event.getChannel()
                         .sendMessage(":white_check_mark: Finished queuing provided playlist. Successfully queued **" + sources.size() + "** sources");
                }
            };
            thread.start();
        } else {
            AudioSource source = sources.get(0);
            AudioInfo info = source.getInfo();
            if (info.getError() == null) {
                AudioUtil.player.getAudioQueue().add(source);
                msg += ":white_check_mark: The provided URL has been added the to queue";
                if (AudioUtil.player.isStopped()) {
                    AudioUtil.player.play();
                    msg += " and the player has started playing";
                }
                event.getChannel().sendMessage(msg + ".");
            } else {
                event.getChannel().sendMessage(":x: There was an error while loading the provided URL.\n" +
                                                       "Error: " + info.getError());
            }
        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "play");
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
