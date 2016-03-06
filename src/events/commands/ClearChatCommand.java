package events.commands;

import bots.RunBot;
import misc.Permissions;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/15/16.
 */
public class ClearChatCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (PermissionUtil.checkPermission(RunBot.BOT, Permission.MESSAGE_MANAGE, e.getTextChannel()) && PermissionUtil.checkPermission(RunBot.BOT, Permission.MESSAGE_HISTORY, e.getTextChannel())) {
            if (!Permissions.getPermissions().isOp(e.getAuthor())) {
                e.getChannel().sendMessage("Sorry, this command is OP only!");
                return;
            }
            clearChat(e, args);
        } else
            e.getChannel().sendMessage("I do not have permissions sufficient to complete this task.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "clch", RunBot.prefix + "clear", RunBot.prefix + "clearChat", RunBot.prefix + "clearchat");
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
    public List<String> getUsageInstructions() {
        return Collections.singletonList(RunBot.prefix + "clch <@mentions>");
    }

    private void clearChat(MessageReceivedEvent event, String[] commandArguments) {

        MessageHistory history = new MessageHistory(event.getJDA(), event.getTextChannel());

        if (commandArguments.length < 2)
            // if there was no arguments do nothing
            return;
        else if (commandArguments[1].equals("@everyone")) {
            // deletes entire history
            history.retrieveAll().parallelStream().forEach(k -> k.deleteMessage());
        } else {
            List<User> users = event.getMessage().getMentionedUsers();
            deleteAllMessagesOfMultipleUsers(users, history);
            event.getChannel().sendMessage(buildDeleteMessageForMultipleUsers(users));
        }

    }

    private void deleteAllMessagesOfMultipleUsers(List<User> users, MessageHistory history) {
        history.retrieveAll().parallelStream().forEach(x -> users.forEach(k -> {
            if (k != null && x.getAuthor().getId().equals(k.getId()))
                x.deleteMessage();
        }));
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
