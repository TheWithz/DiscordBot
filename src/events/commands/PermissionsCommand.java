package events.commands;

import bots.RunBot;
import misc.Permissions;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class PermissionsCommand extends Command {


    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (RunBot.OwnerRequired(e))
            return;

        if (args[0].contains(RunBot.PREFIX + "perms") || args[0].contains(RunBot.PREFIX + "permissions")) {
            args = ArrayUtils.subarray(args, 1, args.length);   //We cut off the .perms or .permissions to make the array behave as .op would
        } else {
            args[0] = args[0].replace(RunBot.PREFIX + "", "");     //Cut off the leading .
        }

        if (args.length < 1)    //If the command sent was just '.perms', and we removed that above, then we have an array of length 0 currently.
        {
            e.getChannel().sendMessage(":x: **Improper syntax, no permissions group provided!**").queue();
            return;
        }
        switch (args[0]) {
            //Only 1 case for now. Later we will have more user permissions types...probably.
            case "op":
                processOp(e, args);
                break;
            default:
                e.getChannel().sendMessage(new MessageBuilder()
                                                   .appendString(":x: **Improper syntax, unrecognized permission group:** ")
                                                   .appendString(args[0])
                                                   .appendString("\n**Provided Command:** ")
                                                   .appendString(e.getMessage().getContent())
                                                   .build()).queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "perms", RunBot.PREFIX + "permissions", RunBot.PREFIX + "op");
    }

    @Override
    public String getDescription() {
        return "Used to modify the permissions of the provided user.";
    }

    @Override
    public String getName() {
        return "Permissions Management";
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
        return Collections.singletonList(
                String.format("(%1$s)] <group> <action> <user>\n"
                                      + "[Groups:][op]\n"
                                      + "[Actions:][add, remove, list]\n"
                                      + "[User:][Must be an @Mentioned user]\n"
                                      + "[Example 1:](%1$s) <op> <add> <@ TheWithz> This would add the user <TheWithz> to the OPs list.\n"
                                      + "[Example 2:](%1$s) <op> <list> This would list all bot OPs.\n"
                                      + "NOTE: you can skip <%1$s> and jump straight to the group by using the group alias <%2$s>.\n"
                                      + "[Example:](%2$s) <remove> <@ BananaPhone>", getAliases().get(0), getAliases().get(2)));
    }

    private void processOp(MessageReceivedEvent e, String[] args) {
        if (args.length < 2) {
            e.getChannel().sendMessage(":x: **Improper syntax, no action argument provided!**").queue();
            return;
        }
        switch (args[1]) {
            case "add":
                processAddOp(e, args);
                break;
            case "remove":
                processRemoveOp(e, args);
                break;
            case "list":
                boolean notFirstLoop = false;
                StringBuilder builder = new StringBuilder();
                builder.append(":white_check_mark: My OPs are:  [");
                for (String op : Permissions.getPermissions().getOps()) {
                    if (notFirstLoop)
                        builder.append(", ");
                    User user = e.getJDA().getUserById(op);
                    if (user != null)
                        builder.append(user.getName());
                    else
                        builder.append("<@").append(op).append(">");
                    notFirstLoop = true;
                }
                builder.append("]");
                e.getChannel().sendMessage(builder.toString()).queue();
                break;
            default:
                e.getChannel().sendMessage(new MessageBuilder()
                                                   .appendString(":x: **Improper syntax, unrecognized argument:** ")
                                                   .appendString(args[1])
                                                   .appendString("\n**Provided Command:** ")
                                                   .appendString(e.getMessage().getContent())
                                                   .build()).queue();
        }
    }

    private void processAddOp(MessageReceivedEvent e, String[] args) {
        if (args.length < 3 || e.getMessage().getMentionedUsers().isEmpty()) {
            e.getChannel().sendMessage(":x: Please provide a user!").queue();
            return;
        }

        for (User user : e.getMessage().getMentionedUsers()) {
            try {
                if (Permissions.getPermissions().addOp(user.getId())) {
                    e.getChannel().sendMessage(":white_check_mark: Successfully added " + user.getName() + " to the OPs list!").queue();
                    return;
                } else {
                    e.getChannel().sendMessage(user.getName() + " is already an OP!").queue();
                    return;
                }
            } catch (Exception e1) {
                e.getChannel().sendMessage(new MessageBuilder()
                                                   .appendString(":x: Encountered an error when attempting to add OP.\n")
                                                   .appendString("User: ").appendString(user.getName())
                                                   .appendString("Error: ").appendString(e1.getClass().getName()).appendString("\n")
                                                   .appendString("Reason: ").appendString(e1.getMessage())
                                                   .build()).queue();
            }
        }
    }

    private void processRemoveOp(MessageReceivedEvent e, String[] args) {
        if (args.length < 3 || e.getMessage().getMentionedUsers().isEmpty()) {
            e.getChannel().sendMessage(":x: Please provide a user!").queue();
            return;
        }
        //Pattern idPattern = Pattern.compile("(?<=<@)[0-9]{18}(?=>)");
        //Matcher idMatch = idPattern.matcher(args[2]);
        //if (!idMatch.find()) {
        //    e.getChannel().sendMessageAsync("Sorry, I don't recognize the user provided: " + args[2]);
        //    return;
        //}
        for (User user : e.getMessage().getMentionedUsers()) {
            try {
                if (Permissions.getPermissions().removeOp(user.getId())) {
                    e.getChannel().sendMessage(":white_check_mark: Successfully removed " + user.getName() + " to the OPs list!").queue();
                    return;
                } else {
                    e.getChannel().sendMessage(":x: " + user.getName() + " cannot be removed because they weren't an OP!").queue();
                    return;
                }
            } catch (Exception e1) {
                e.getChannel().sendMessage(new MessageBuilder()
                                                   .appendString(":x: Encountered an error when attempting to remove OP.\n")
                                                   .appendString("User: ").appendString(user.getName())
                                                   .appendString("Error: ").appendString(e1.getClass().getName()).appendString("\n")
                                                   .appendString("Reason: ").appendString(e1.getMessage())
                                                   .build()).queue();
            }
        }
    }
}