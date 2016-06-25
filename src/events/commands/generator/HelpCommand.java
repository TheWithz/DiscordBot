package events.commands.generator;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static misc.Permissions.Perm;
import static misc.Permissions.Perm.*;
import static misc.Permissions.getPermissions;

public class HelpCommand extends Command {
    private static final String NO_NAME = "No name provided for this command. Sorry!";
    private static final String NO_DESCRIPTION = "No description has been provided for this command. Sorry!";
    private static final String NO_USAGE = "No usage instructions have been provided for this command. Sorry!";

    private ArrayList<Command> commands;

    public HelpCommand() {
        commands = new ArrayList<Command>();
    }

    public Command registerCommand(Command command, Perm permission) {
        commands.add(command.registerPermission(permission));
        return command;
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (e.getAuthor().getId().equals("122764399961309184"))
            sendConfirmation(e, args, OWNER_ONLY);
        else if (getPermissions().isOp(e.getAuthor()))
            sendConfirmation(e, args, OP_ONLY);
        else
            sendConfirmation(e, args, EVERYONE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "help", RunBot.PREFIX + "commands");
    }

    @Override
    public String getDescription() {
        return "Command that helps use all other commands!";
    }

    @Override
    public String getName() {
        return "Help Command";
    }

    @Override
    public List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(
                getAliases().get(0) + " **OR** " + getAliases().get(0) + " *<command>*\n"
                        + getAliases().get(0) + " | returns the list of commands along with a simple description of each.\n"
                        + getAliases().get(0) + " <command> | returns the name, description, aliases, and usage information of a command.\n"
                        + "   - This can use the aliases of a command as input as well.\n"
                        + "__Example:__ " + getAliases().get(0) + " stats");
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return getUsageInstructionsEveryone();
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return getUsageInstructionsEveryone();
    }

    private void sendConfirmation(MessageReceivedEvent e, String[] args, Perm permission) {
        if (!e.isPrivate()) {
            e.getTextChannel().sendMessage(new MessageBuilder()
                                                   .appendMention(e.getAuthor())
                                                   .appendString(": Help information was sent as a private message.")
                                                   .build());
        }
        sendPrivate(e.getAuthor().getPrivateChannel(), args, permission);
    }

    private void handleEveryoneHelp(PrivateChannel channel, String[] args) {

    }

    private void handleOpHelp(PrivateChannel channel, String[] args) {

    }

    private void handleOwnerHelp(PrivateChannel channel, String[] args) {
        if (args.length < 2) {
            StringBuilder s = new StringBuilder();
            for (Command c : commands) {
                String description = c.getDescription();
                description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

                s.append(c.getAliases().get(0).substring(RunBot.PREFIX.length())).append("][");
                s.append(description).append("]\n\n");
            }

            channel.sendMessage(new MessageBuilder()
                                        .appendString("```fix\nThe following commands are supported by the bot```")
                                        .appendString("```\nUse ")
                                        .appendString(RunBot.PREFIX)
                                        .appendString(" to instantiate a command.```")
                                        .appendString("```md\n")
                                        .appendString(s.toString())
                                        .appendString("```")
                                        .build());
        } else {
            String command = ((args[1].length() > RunBot.PREFIX.length() + 1) && args[1].substring(0, RunBot.PREFIX.length()).equals(RunBot.PREFIX)) ?
                    args[1] :
                    RunBot.PREFIX + args[1];    //If there is not a preceding PREFIX attached to the command we are search, then prepend one.
            for (Command c : commands) {
                if (c.getAliases().contains(command)) {
                    String name = c.getName();
                    String description = c.getDescription();
                    List<String> usageInstructions = c.getUsageInstructionsEveryone();
                    name = (name == null || name.isEmpty()) ? NO_NAME : name;
                    description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;
                    usageInstructions = (usageInstructions == null || usageInstructions.isEmpty()) ? Collections.singletonList(NO_USAGE) : usageInstructions;

                    channel.sendMessage(new MessageBuilder()
                                                .appendString("**Name:** " + name + "\n")
                                                .appendString("**Description:** " + description + "\n")
                                                .appendString("**Alliases:** " + StringUtils.join(c.getAliases(), ", ") + "\n")
                                                .appendString("**Usage:** ")
                                                .appendString(usageInstructions.get(0))
                                                .build());
                    for (int i = 1; i < usageInstructions.size(); i++) {
                        channel.sendMessage(new MessageBuilder()
                                                    .appendString("__" + name + " Usage Cont. (" + (i + 1) + ")__\n")
                                                    .appendString(usageInstructions.get(i))
                                                    .build());
                    }
                    return;
                }
            }
            channel.sendMessage(new MessageBuilder()
                                        .appendString(":x: The provided command '**" + args[1] + "**' does not exist. Use " + RunBot.PREFIX + "help to list all commands.")
                                        .build());
        }
    }

    private void sendPrivate(PrivateChannel channel, String[] args, Perm permission) {
        switch (permission) {
            case EVERYONE:
                handleEveryoneHelp(channel, args);
                break;
            case OP_ONLY:
                handleOpHelp(channel, args);
                break;
            case OWNER_ONLY:
                handleOwnerHelp(channel, args);
                break;
        }
    }
}