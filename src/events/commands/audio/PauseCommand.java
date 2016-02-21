package events.commands.audio;

import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class PauseCommand extends Command implements AudioUtil {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        pause(e);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("$pause");
    }

    @Override
    public String getDescription() {
        return "Command that pauses Audio";
    }

    @Override
    public String getName() {
        return "Pause Command";
    }

    @Override
    public String getUsageInstructions() {
        return "$pause";
    }

    private void pause(MessageReceivedEvent e) {
        if (FILEPLAYER.player == null) {
            System.out.println("player is null");
            e.getChannel().sendMessage("hmmm... something went wrong that shouldn't have.");
        } else if (FILEPLAYER.player.isPaused() || FILEPLAYER.player.isStopped()) {
            e.getChannel().sendMessage("You cannot pause before you play!");
        } else
            FILEPLAYER.player.pause();
    }

}
