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

        //If no URL was provided.
        if (args.length == 1 && args[0].equals(RunBot.prefix + "play")) {
            if (AudioUtil.player.isPlaying()) {
                event.getChannel().sendMessage("player is already playing!");
                return;
            } else if (AudioUtil.player.isPaused()) {
                AudioUtil.player.play();
                event.getChannel().sendMessage("playback as been resumed.");
            } else {
                if (AudioUtil.player.getAudioQueue().isEmpty())
                    event.getChannel()
                         .sendMessage("The current audio queue is empty! Add something to the queue first!");
                else {
                    AudioUtil.player.play();
                    event.getChannel().sendMessage("player has started playing!");
                }
            }
        } else if (args.length == 2 && args[1] != null) {
            String msg = "";
            String url = args[1];
            Playlist playlist = Playlist.getPlaylist(url);
            List<AudioSource> sources = new LinkedList(playlist.getSources());
//                AudioSource source = new RemoteSource(url);
//                AudioSource source = new LocalSource(new File(url));
//                AudioInfo info = source.getInfo();   //Preload the audio info.
            if (sources.size() > 1) {
                event.getChannel().sendMessage("Found a playlist with **" + sources.size() + "** entries.\n" +
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
                                     .sendMessage("Error detected, skipping source. Error:\n" + info.getError());
                                it.remove();
                            }
                        }
                        event.getChannel()
                             .sendMessage("Finished queuing provided playlist. Successfully queued **" + sources.size() + "** sources");
                    }
                };
                thread.start();
            } else {
                AudioSource source = sources.get(0);
                AudioInfo info = source.getInfo();
                if (info.getError() == null) {
                    AudioUtil.player.getAudioQueue().add(source);
                    msg += "The provided URL has been added the to queue";
                    if (AudioUtil.player.isStopped()) {
                        AudioUtil.player.play();
                        msg += " and the player has started playing";
                    }
                    event.getChannel().sendMessage(msg + ".");
                } else {
                    event.getChannel().sendMessage("There was an error while loading the provided URL.\n" +
                                                           "Error: " + info.getError());
                }
            }
        } else if (args.length == 3 && args[1].equals("playlist") && args[2] != null) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Playlists.json"))));
                        event.getChannel().sendMessage(RunBot.prefix + "play \"" + obj.getString(args[2]) + "\"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.prefix + "play");
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
