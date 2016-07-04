package events.commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * Created by thewithz on 7/3/16.
 */
public class MistakeCommand extends Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        super.onMessageReceived(e);
        if (e.getMessage().getContent().equals("fuck")) {
            e.getChannel().sendMessage(findMatchingCommand());
        }
    }

    private String findMatchingCommand() {
        return "";
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {

    }

    @Override
    public List<String> getAliases() {
        return null;
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
    public List<String> getUsageInstructionsEveryone() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return null;
    }
}
