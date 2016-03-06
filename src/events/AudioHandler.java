package events;

/**
 * Created by TheWithz on 2/15/16.
 */

import bots.RunBot;
import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.audio.player.Player;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AudioHandler extends ListenerAdapter {
    Player player = null;
    VoiceChannel curChannel = null;
    File musicDir = new File("/home/TheWithz/Music/");

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContent();

        //Start an audio connection with a VoiceChannel
        if (message.startsWith(RunBot.prefix + "join ")) {
            //Separates the name of the channel so that we can search for it
            String chanName = message.substring(6);

            //Scans through the VoiceChannels in this Guild, looking for one with a case-insensitive matching name.
            curChannel = event.getGuild().getVoiceChannels().stream().filter(
                    vChan -> vChan.getName().equalsIgnoreCase(chanName))
                    .findFirst().orElse(null);  //If there isn't a matching name, return null.
            if (curChannel == null) {
                System.out.println("There isn't a VoiceChannel in this Guild with the name: '" + chanName + "'");
                return;
            }
            event.getJDA().getAudioManager().openAudioConnection(curChannel);
        }
        //Disconnect the audio connection with the VoiceChannel.
        if (message.equals(RunBot.prefix + "leave"))
            event.getJDA().getAudioManager().closeAudioConnection();

        //Start playing audio with our FilePlayer. If we haven't created and registered a FilePlayer yet, do that.
        if (message.equals(RunBot.prefix + "play")) {
            //If the player didn't exist, create it and start playback.
            if (player == null) {
                File audioFile = null;
                URL audioUrl = null;
                try {
                    System.out.println("/home/TheWithz/Music/" + musicDir.list()[(int) (musicDir.list().length * Math.random())]);
                    audioFile = new File("/home/TheWithz/Music/" + musicDir.list()[(int) (musicDir.list().length * Math.random())]);
//                    audioUrl = new URL("https://dl.dropboxusercontent.com/u/41124983/anime-48000.mp3?dl=1");

                    player = new FilePlayer(audioFile);
//                    player = new URLPlayer(event.getJDA(), audioUrl);

                    //Provide the handler to send audio.
                    //NOTE: You don't have to set the handler each time you create an audio connection with the
                    // AudioManager. Handlers persist between audio connections. Furthermore, handler playback is also
                    // paused when a connection is severed (closeAudioConnection), however it would probably be better
                    // to pause the play back yourself before severing the connection (If you are using a player class
                    // you could just call the pause() method. Otherwise, make canProvide() return false).
                    // Once again, you don't HAVE to pause before severing an audio connection,
                    // but it probably would be good to do.
                    event.getJDA().getAudioManager().setSendingHandler(player);

                    //Start playback. This will only start after the AudioConnection has completely connected.
                    //NOTE: "completely connected" is not just joining the VoiceChannel. Think about when your Discord
                    // client joins a VoiceChannel. You appear in the channel lobby immediately, but it takes a few
                    // moments before you can start communicating.
                    player.play();
                } catch (IOException e) {
                    System.out.println("Could not load the file. Does it exist?  File name: " + audioFile.getName());
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    System.out.println("Could not load file. It either isn't an audio file or isn't a" +
                            " recognized audio format.");
                    e.printStackTrace();
                }
            } else if (player.isStarted() && player.isStopped())  //If it did exist, has it been stop()'d before?
            {
                System.out.println("The player has been stopped. To start playback, please use '$restart'");
                return;
            } else    //It exists and hasn't been stopped before, so play. Note: if it was already playing, this will have no effect.
            {
                player.play();
            }

        }

        //You can't pause, stop or restart before a player has even been created!
        if (player == null && (message.equals(RunBot.prefix + "pause") || message.equals(RunBot.prefix + "stop") || message.equals(RunBot.prefix + "restart"))) {
            System.out.println("You need to 'play' before you can preform that command.");
            return;
        }

        if (player != null) {
            if (message.equals(RunBot.prefix + "pause"))
                player.pause();
            if (message.equals(RunBot.prefix + "stop"))
                player.stop();
            if (message.equals(RunBot.prefix + "restart"))
                player.restart();
        }
    }
}
