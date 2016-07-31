package events.commands.music;

import net.dv8tion.jda.audio.AudioReceiveHandler;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;

/**
 * Created by TheWithz on 4/24/16.
 */
public class AudioUtil {
    public static final float DEFAULT_VOLUME = 0.35f;
    public static AudioManager manager;
    public static MusicPlayer player;

    public static void setManagerAndPlayerForSending(MessageReceivedEvent event) {
        manager = event.getGuild().getAudioManager();
        if (manager.getSendingHandler() == null) {
            player = new MusicPlayer();
            player.setVolume(DEFAULT_VOLUME);
            manager.setSendingHandler(player);
        } else {
            player = (MusicPlayer) manager.getSendingHandler();
        }
    }

    public static void setManagerAndPlayerForRecieving(MessageReceivedEvent event, AudioReceiveHandler handler) {
        manager = event.getGuild().getAudioManager();
        if (manager.getReceiveHandler() == null) {
            manager.setReceivingHandler(handler);
        }
    }
}
