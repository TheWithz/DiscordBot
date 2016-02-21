package events.commands.audio;

import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.audio.player.Player;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * Created by TheWithz on 2/21/16.
 */
public interface AudioUtil {

    File musicDir = new File("/home/TheWithz/Music/");
    File audioFile = new File(musicDir.getPath() + "/" + musicDir.list()[(int) (musicDir.list().length * Math.random())]);

    public static class FILE_PLAYER {
        static Player player;

        FILE_PLAYER() {
            try {
                player = new FilePlayer(audioFile);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not load the file. Does it exist?  File name: " + audioFile.getName());
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
                System.out.println("Could not load file. It either isn't an audio file or isn't a" +
                        " recognized audio format.");
            }
        }
    }
}

