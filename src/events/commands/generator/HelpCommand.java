package events.commands.generator;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        return Collections.singletonList(String.format("(%1$s)\n" +
                                                               "[Example: 1](%1$s) Shows all <%2$s's> commands with permission level <%3$s>\n" +
                                                               "[Example: 2](%1$s) <todo> Shows <%2$s's> usage information for the todo command if it is in the <%3$s> permission level", getAliases()
                                                               .get(0), RunBot.BOT.getName(), Perm.EVERYONE));
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return Collections.singletonList(String.format("(%1$s)\n" +
                                                               "[Example: 1](%1$s) Shows all <%2$s's> commands with permission level <%3$s> and <%4$s>\n" +
                                                               "[Example: 2](%1$s) <todo> Shows <%2$s's> usage information for the todo command if it is in the <%3$s> or <%4$s> permission level", getAliases()
                                                               .get(0), RunBot.BOT.getName(), Perm.EVERYONE, Perm.OP_ONLY));
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return Collections.singletonList(String.format("(%1$s)\n" +
                                                               "[Example: 1](%1$s) Shows all <%2$s's> commands with permission level <%3$s> and <%4$s> and <%5$s>\n" +
                                                               "[Example: 2](%1$s) <todo> Shows <%2$s's> usage information for the todo command if it is in the <%3$s> or <%4$s> or <%5$s> permission level", getAliases()
                                                               .get(0), RunBot.BOT.getName(), Perm.EVERYONE, Perm.OP_ONLY, Perm.OWNER_ONLY));
    }

    private void sendConfirmation(MessageReceivedEvent e, String[] args, Perm permission) {
        if (!e.isFromType(ChannelType.PRIVATE)) {
            e.getTextChannel().sendMessage(new MessageBuilder()
                                                   .appendMention(e.getAuthor())
                                                   .appendString(": Help information was sent as a private message.")
                                                   .build()).queue();
        }
        if (e.getAuthor().hasPrivateChannel())
            sendPrivate(e.getAuthor().getPrivateChannel(), args, permission);
    }

    private void handleEveryoneHelp(PrivateChannel channel, String[] args) {
        if (args.length < 2)
            handleEveryoneAll(channel);
        else if (args.length < 3)
            handleEveryoneSingle(channel, args);
    }

    private void handleEveryoneAll(PrivateChannel channel) {
        StringBuilder s = new StringBuilder();
        ArrayList<StringBuilder> msgs = new ArrayList<>();
        for (Command c : commands) {
            if (c.permission == Perm.OWNER_ONLY || c.permission == Perm.OP_ONLY)
                continue;

            String description = c.getDescription();
            description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

            s.append("[" + RunBot.PREFIX + "][")
             .append(c.getAliases().get(0).substring(RunBot.PREFIX.length()))
             .append("] : [")
             .append(description)
             .append("](")
             .append(c.permission)
             .append(")")
             .append("\n\n");
            if (s.length() >= 1800) {
                msgs.add(s);
                s = new StringBuilder();
            }
        }
        if (s.length() < 1800 && s.length() > 0)
            msgs.add(s);
        channel.sendMessage(new MessageBuilder()
                                    .appendString("```css\nThe following commands are supported by the bot```").build()).queue();
        for (StringBuilder builder : msgs) {
            channel.sendMessage(new MessageBuilder()
                                        .appendString("```md\n")
                                        .appendString(builder.toString())
                                        .appendString("```")
                                        .build()).queue();
        }

    }

    private void handleEveryoneSingle(PrivateChannel channel, String[] args) {
        String command = ((args[1].length() > RunBot.PREFIX.length() + 1) && args[1].substring(0, RunBot.PREFIX.length()).equals(RunBot.PREFIX)) ?
                args[1] :
                RunBot.PREFIX + args[1];    //If there is not a preceding PREFIX attached to the command we are search, then prepend one.
        for (Command c : commands) {
            if (c.getAliases().contains(command)) {
                if (c.permission == Perm.OWNER_ONLY || c.permission == Perm.OP_ONLY) {
                    channel.sendMessage(":no_entry: You do not have permission to view this command.").queue();
                    return;
                }
                String name = c.getName();
                String description = c.getDescription();
                List<String> usageInstructions = c.getUsageInstructionsEveryone();
                name = (name == null || name.isEmpty()) ? NO_NAME : name;
                description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;
                usageInstructions = (usageInstructions == null || usageInstructions.isEmpty()) ? Collections.singletonList(NO_USAGE) : usageInstructions;

                channel.sendMessage(new MessageBuilder()
                                            .appendString("```md\n")
                                            .appendString("[Name:][" + name + "]\n")
                                            .appendString("[Description:][" + description + "]\n")
                                            .appendString("[Alliases:](" + StringUtils.join(c.getAliases(), ", ") + ")\n")
                                            .appendString("[[Usage:]")
                                            .appendString(usageInstructions.get(0))
                                            .appendString("```")
                                            .build()).queue();
                for (int i = 1; i < usageInstructions.size(); i++) {
                    channel.sendMessage(new MessageBuilder()
                                                .appendString("```md\n")
                                                .appendString("[" + name + "](Usage Cont. " + (i + 1) + ")\n")
                                                .appendString(usageInstructions.get(i))
                                                .appendString("```")
                                                .build()).queue();
                }
                return;
            }
        }
        channel.sendMessage(new MessageBuilder()
                                    .appendString(":x: The provided command '**" + args[1] + "**' does not exist. Use " + RunBot.PREFIX + "help to list all commands.")
                                    .build()).queue();
    }

    private void handleOpHelp(PrivateChannel channel, String[] args) {
        if (args.length < 2)
            handleOpAll(channel);
        else if (args.length < 3)
            handleOpSingle(channel, args);
    }

    private void handleOpAll(PrivateChannel channel) {
        StringBuilder s = new StringBuilder();
        ArrayList<StringBuilder> msgs = new ArrayList<>();
        for (Command c : commands) {
            if (c.permission == Perm.OWNER_ONLY)
                continue;

            String description = c.getDescription();
            description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

            s.append("[" + RunBot.PREFIX + "][")
             .append(c.getAliases().get(0).substring(RunBot.PREFIX.length()))
             .append("] : [")
             .append(description)
             .append("](")
             .append(c.permission)
             .append(")")
             .append("\n\n");
            if (s.length() >= 1800) {
                msgs.add(s);
                s = new StringBuilder();
            }
        }
        if (s.length() < 1800 && s.length() > 0)
            msgs.add(s);
        channel.sendMessage(new MessageBuilder()
                                    .appendString("```css\nThe following commands are supported by the bot```").build()).queue();
        for (StringBuilder builder : msgs) {
            channel.sendMessage(new MessageBuilder()
                                        .appendString("```md\n")
                                        .appendString(builder.toString())
                                        .appendString("```")
                                        .build()).queue();
        }

    }

    private void handleOpSingle(PrivateChannel channel, String[] args) {
        String command = ((args[1].length() > RunBot.PREFIX.length() + 1) && args[1].substring(0, RunBot.PREFIX.length()).equals(RunBot.PREFIX)) ?
                args[1] :
                RunBot.PREFIX + args[1];    //If there is not a preceding PREFIX attached to the command we are search, then prepend one.

        for (Command c : commands) {
            if (c.getAliases().contains(command)) {
                if (c.permission == Perm.OWNER_ONLY) {
                    channel.sendMessage(":no_entry: You do not have permission to view this command.").queue();
                    return;
                }
                String name = c.getName();
                String description = c.getDescription();
                List<String> usageInstructions = c.getUsageInstructionsOp();
                name = (name == null || name.isEmpty()) ? NO_NAME : name;
                description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;
                usageInstructions = (usageInstructions == null || usageInstructions.isEmpty()) ? Collections.singletonList(NO_USAGE) : usageInstructions;

                channel.sendMessage(new MessageBuilder()
                                            .appendString("```md\n")
                                            .appendString("[Name:][" + name + "]\n")
                                            .appendString("[Description:][" + description + "]\n")
                                            .appendString("[Alliases:](" + StringUtils.join(c.getAliases(), ", ") + ")\n")
                                            .appendString("[[Usage:]")
                                            .appendString(usageInstructions.get(0))
                                            .appendString("```")
                                            .build()).queue();
                for (int i = 1; i < usageInstructions.size(); i++) {
                    channel.sendMessage(new MessageBuilder()
                                                .appendString("```md\n")
                                                .appendString("[" + name + "](Usage Cont. " + (i + 1) + ")\n")
                                                .appendString(usageInstructions.get(i))
                                                .appendString("```")
                                                .build()).queue();
                }
                return;
            }
        }
        channel.sendMessage(new MessageBuilder()
                                    .appendString(":x: The provided command '**" + args[1] + "**' does not exist. Use " + RunBot.PREFIX + "help to list all commands.")
                                    .build()).queue();
    }

    private void handleOwnerHelp(PrivateChannel channel, String[] args) {
        if (args.length < 2)
            handleOwnerAll(channel);
        else if (args.length < 3)
            handleOwnerSingle(channel, args);
    }

    private void handleOwnerAll(PrivateChannel channel) {
        StringBuilder s = new StringBuilder();
        ArrayList<StringBuilder> msgs = new ArrayList<>();
        for (Command c : commands) {
            String description = c.getDescription();
            description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

            s.append("[" + RunBot.PREFIX + "][")
             .append(c.getAliases().get(0).substring(RunBot.PREFIX.length()))
             .append("] : [")
             .append(description)
             .append("](")
             .append(c.permission)
             .append(")")
             .append("\n\n");
            if (s.length() >= 1800) {
                msgs.add(s);
                s = new StringBuilder();
            }
        }
        if (s.length() < 1800 && s.length() > 0)
            msgs.add(s);
        channel.sendMessage(new MessageBuilder()
                                    .appendString("```css\nThe following commands are supported by the bot```").build()).queue();
        for (StringBuilder builder : msgs) {
            channel.sendMessage(new MessageBuilder()
                                        .appendString("```md\n")
                                        .appendString(builder.toString())
                                        .appendString("```")
                                        .build()).queue();
        }
    }

    private void handleOwnerSingle(PrivateChannel channel, String[] args) {
        String command = ((args[1].length() > RunBot.PREFIX.length() + 1) && args[1].substring(0, RunBot.PREFIX.length()).equals(RunBot.PREFIX)) ?
                args[1] :
                RunBot.PREFIX + args[1];    //If there is not a preceding PREFIX attached to the command we are search, then prepend one.
        for (Command c : commands) {
            if (c.getAliases().contains(command)) {
                if (c.getName().equals("Calculator")) {
                    testEmbedsAndJson(c, channel);
                    return;
                }
                String name = c.getName();
                String description = c.getDescription();
                List<String> usageInstructions = c.getUsageInstructionsOwner();
                name = (name == null || name.isEmpty()) ? NO_NAME : name;
                description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;
                usageInstructions = (usageInstructions == null || usageInstructions.isEmpty()) ? Collections.singletonList(NO_USAGE) : usageInstructions;

                channel.sendMessage(new MessageBuilder()
                                            .appendString("```md\n")
                                            .appendString("[Name:][" + name + "]\n")
                                            .appendString("[Description:][" + description + "]\n")
                                            .appendString("[Alliases:](" + StringUtils.join(c.getAliases(), ", ") + ")\n")
                                            .appendString("[[Usage:]")
                                            .appendString(usageInstructions.get(0))
                                            .appendString("```")
                                            .build()).queue();
                for (int i = 1; i < usageInstructions.size(); i++) {
                    channel.sendMessage(new MessageBuilder()
                                                .appendString("```md\n")
                                                .appendString("[" + name + "](Usage Cont. " + (i + 1) + ")\n")
                                                .appendString(usageInstructions.get(i))
                                                .appendString("```")
                                                .build()).queue();
                }
                return;
            }
        }
        channel.sendMessage(new MessageBuilder()
                                    .appendString(":x: The provided command '**" + args[1] + "**' does not exist. Use " + RunBot.PREFIX + "help to list all commands.")
                                    .build()).queue();
    }

    private void testEmbedsAndJson(Command c, PrivateChannel channel) {
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("Help.json"))));
            JSONObject commandObject = obj.getJSONObject(c.getName().toUpperCase());
            JSONObject permissionObject = commandObject.getJSONObject(c.permission.name());
            String name = permissionObject.getString("name");
            String description = permissionObject.getString("description");
            JSONArray aliases = permissionObject.getJSONArray("aliases");
            JSONArray usages = permissionObject.getJSONArray("usages");
            JSONArray examples = permissionObject.getJSONArray("aliases");
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Help for " + c.getName())
                   .addField("Name", c.getName(), true)
                   .addField("\u200E", "\u200E", true)
                   .addField("Permission Level", c.permission.name(), true)
                   .addField("Description", "```css\n" + description + "```", false);
            aliases.forEach(alias -> builder.addField("\u200E", "```md\n" + alias + "```", false));
            // name
            // description
            // alias
            // usage
            // example
            builder.build();
            channel.sendMessage(new MessageBuilder().setEmbed(builder.build())
                                                    .build())
                   .queue();
        } catch (IOException e) {
            e.printStackTrace();
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