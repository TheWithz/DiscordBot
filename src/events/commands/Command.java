package events.commands;

import misc.Permissions;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public abstract class Command extends ListenerAdapter {
    public Permissions.Perm permission = Permissions.Perm.EVERYONE;

    public abstract void onCommand(MessageReceivedEvent e, String[] args);

    public abstract List<String> getAliases();

    public abstract String getDescription();

    public abstract String getName();

    public abstract List<String> getUsageInstructionsEveryone();

    public abstract List<String> getUsageInstructionsOp();

    public abstract List<String> getUsageInstructionsOwner();

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContent().length() > 0 && containsCommand(e.getMessage()))
            onCommand(e, commandArgs(e.getMessage()));
    }

    private boolean containsCommand(Message message) {
        return getAliases().contains(commandArgs(message)[0].toLowerCase());
    }

    private String[] commandArgs(Message message) {
        return commandArgs(message.getContent());
    }

    private String[] commandArgs(String string) {
        ArgParse parser = new ArgParse();
        return parser.parse(string);
    }

    protected Message sendMessage(MessageReceivedEvent e, Message message) {
        if (e.isPrivate())
            return e.getPrivateChannel().sendMessage(message);
        else
            return e.getTextChannel().sendMessage(message);
    }

    protected Message sendMessage(MessageReceivedEvent e, String message) {
        return sendMessage(e, new MessageBuilder().appendString(message).build());
    }

    public Command registerPermission(Permissions.Perm permission) {
        this.permission = permission;
        return this;
    }
}