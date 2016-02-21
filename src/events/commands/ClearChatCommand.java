package events.commands;

import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/15/16.
 */
public class ClearChatCommand extends Command {
    private static final String NO_NAME = "No name provided for this Command. Sorry!";
    private static final String NO_DESCRIPTION = "No description has been provided for this Command. Sorry!";
    private static final String NO_USAGE = "No usage instructions have been provided for this Command. Sorry!";

    private ArrayList<Command> Commands;

    public Command registerCommand(Command Command) {
        Commands.add(Command);
        return Command;
    }

    public ClearChatCommand() {
        Commands = new ArrayList<Command>();
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        clearChat(e, e.getMessage().getContent().split("\\s+"));
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("$clch", "$clear", "$clearChat", "$clearchat");
    }

    @Override
    public String getDescription() {
        return "Command that clears chat.";
    }

    @Override
    public String getName() {
        return "Clear Chat Command";
    }

    @Override
    public String getUsageInstructions() {
        return "$clch <@mentions> \"as many as you want.\"" + "\n" + "$clch all \"in place of @everyone. which does NOT work\"";
    }

    private void clearChat(MessageReceivedEvent event, String[] commandArguments) {

        MessageHistory history = new MessageHistory(event.getJDA(), event.getTextChannel());

        if (commandArguments.length < 2)
            // if there was no arguments do nothing
            return;
        else if (commandArguments[1].equals("@everyone")) {
            // deletes entire history
            history.retrieveAll().forEach(k -> k.deleteMessage());
        } else {
            List<User> users = event.getMessage().getMentionedUsers();
            deleteAllMessagesOfMultipleUsers(users, history);
            System.out.println(buildDeleteMessageForMultipleUsers(users));
        }

    }

    private void deleteAllMessagesOfMultipleUsers(List<User> users, MessageHistory history) {
        for (Message x : history.retrieveAll()) {
            // deletes history of specific individuals
            users.forEach(k -> {
                if (k != null && x.getAuthor().getId().equals(k.getId()))
                    x.deleteMessage();
            });
        }
    }

    private String buildDeleteMessageForMultipleUsers(List<User> users) {
        StringBuilder builder = new StringBuilder();
        builder.append("I deleted all of ");
        for (int i = 0; i < users.size(); i++) {
            builder.append(users.get(i).getUsername()).append("'s and ");
            if (i == users.size() - 1) {
                builder.delete(builder.lastIndexOf(" and"), builder.length());
                builder.append(" messages.");
                break;
            }
        }
        return builder.toString();
    }
}
