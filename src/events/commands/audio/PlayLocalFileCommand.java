package events.commands.audio;

import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class PlayLocalFileCommand extends Command implements AudioUtil {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        play(e);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("$play");
    }

    @Override
    public String getDescription() {
        return "Command that plays music!";
    }

    @Override
    public String getName() {
        return "Play Command";
    }

    @Override
    public String getUsageInstructions() {
        // TODO: 2/21/16 explain implementations of play
        return "";
    }

    private void play(MessageReceivedEvent event) {
        AudioUtil.FILE_PLAYER filePlayer = new AudioUtil.FILE_PLAYER();
        event.getJDA().getAudioManager().setSendingHandler(filePlayer.player);
        // TODO: 2/21/16 write other "none random" play command
        if (filePlayer.player == null) {
            filePlayer.player.play();
        } else if (filePlayer.player.isStarted() && filePlayer.player.isStopped()) {
            System.out.println("The player has been stopped. To start playback, please use '$restart'");
            return;
        } else
            filePlayer.player.play();
    }
}
