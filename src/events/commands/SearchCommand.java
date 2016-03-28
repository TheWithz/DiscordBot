package events.commands;

import bots.RunBot;
import misc.GoogleSearch;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class SearchCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
//        if (!Permissions.getPermissions().isOp(e.getAuthor())) {
//            e.getChannel().sendMessage("Sorry, this command is OP only!");
//            return;
//        }

        String filter = null;
        switch (args[0]) {
            case RunBot.prefix + "google":
            case RunBot.prefix + "g":
                break;
            case RunBot.prefix + "wiki":
                filter = "wiki";
                break;
            case RunBot.prefix + "urban":
                filter = "site:urbandictionary.com";
                break;
            default:
                return;
        }

        GoogleSearch search = new GoogleSearch(
                StringUtils.join(args, "+", 1, args.length)
                        + ((filter != null) ? ("+" + filter) : ""));

        sendMessage(e, search.getSuggestedReturn());
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "google", RunBot.prefix + "g", RunBot.prefix + "wiki", RunBot.prefix + "urban");
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
                RunBot.prefix + "google *<search terms>  **OR** " + RunBot.prefix + "wiki *<search terms>*  **OR**  " + RunBot.prefix + "urban *<search terms>*\n");
    }
}