package events.commands;

import bots.RunBot;
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
        RunBot.checkArgs(args, 1, ":x: no query was requested for searching. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);

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
        sendMessage(e, BashCommand.runLinuxCommand("python3 google.py " + builder.toString()).toString());
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "search", RunBot.PREFIX + "google", RunBot.PREFIX + "g", RunBot.PREFIX + "wiki", RunBot.PREFIX + "urban");
    }

    @Override
    public String getDescription() {
        return "Searches the internet for the keywords you provide";
    }

    @Override
    public String getName() {
        return "Search Command";
    }

    @Override
    public List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)] <Search Terms>\n" +
                                                               "[Example:](%1$s) <\"who won the cold war\"> This will search the internet with the string literal <\"who won " +
                                                               "the cold war\"> as a keyword",
                                                       getAliases().get(0)));
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return getUsageInstructionsEveryone();
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return getUsageInstructionsEveryone();
    }
}