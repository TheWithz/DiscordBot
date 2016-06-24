package events.commands;

import bots.RunBot;
import misc.Permissions;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class SearchCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (!Permissions.getPermissions().isOp(e.getAuthor())) {
            e.getChannel().sendMessage("Sorry, this command is OP only!");
            return;
        }

//        String filter = null;
//        switch (args[0]) {
//            case RunBot.PREFIX + "google":
//            case RunBot.PREFIX + "g":
//                break;
//            case RunBot.PREFIX + "wiki":
//                filter = "wiki";
//                break;
//            case RunBot.PREFIX + "urban":
//                filter = "site:urbandictionary.com";
//                break;
//            default:
//                return;
//        }

        StringBuilder builder = new StringBuilder();
        Arrays.asList(args).stream().forEach(builder::append);
        sendMessage(e, LinuxCommand.runLinuxCommand("python resources/html.py " + builder.toString()).toString());
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "google", RunBot.PREFIX + "g", RunBot.PREFIX + "wiki", RunBot.PREFIX + "urban");
    }

    @Override
    public String getDescription() {
        return "Allows you to search Google. The aliases search Wiki sites (not wikipedia exclusive) and Urban Dictionary.";
    }

    @Override
    public String getName() {
        return "Google Search";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(
                RunBot.PREFIX + "google *<search terms>  **OR** " + RunBot.PREFIX + "wiki *<search terms>*  **OR**  " + RunBot.PREFIX + "urban *<search terms>*\n");
    }
}