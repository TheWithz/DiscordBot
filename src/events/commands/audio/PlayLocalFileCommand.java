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
        // TODO: 2/21/16 write other "none random" play command
        if (FILEPLAYER.player == null) {
            event.getJDA().getAudioManager().setSendingHandler(FILEPLAYER.player);
            FILEPLAYER.player.play();
        } else if (FILEPLAYER.player.isStarted() && FILEPLAYER.player.isStopped()) {
            System.out.println("The player has been stopped. To start playback, please use '$restart'");
            return;
        } else
            FILEPLAYER.player.play();
    }
}
