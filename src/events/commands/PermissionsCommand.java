package events.commands;

import bots.RunBot;
import misc.Permissions;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
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
            sendMessage(e, ":x: **Improper syntax, no permissions group provided!**");
            return;
        }
        switch (args[0]) {
            //Only 1 case for now. Later we will have more user permissions types...probably.
            case "op":
                processOp(e, args);
                break;
            default:
                sendMessage(e, new MessageBuilder()
                        .appendString(":x: **Improper syntax, unrecognized permission group:** ")
                        .appendString(args[0])
                        .appendString("\n**Provided Command:** ")
                        .appendString(e.getMessage().getContent())
                        .build());
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
                RunBot.PREFIX + "perms *<group> <action> <user>*\n"
                        + "Groups:  [op]\n"
                        + "Actions: [add, remove, list]\n"
                        + "User:  Must be an @Mentioned user.\n"
                        + "__Example:__  " + RunBot.PREFIX + "perms op add @DV8FromTheWorld\n"
                        + " - This would add the user 'DV8FromTheWorld' to the OPs list.\n"
                        + "__Example 2:__ " + RunBot.PREFIX + "perms op list\n"
                        + " - This would list all bot OPs.\n"
                        + "\n"
                        + "**NOTE:** you can skip the " + RunBot.PREFIX + " perms and jump straight to the group by using the group alias.\n"
                        + "__Example:__ " + RunBot.PREFIX + "op remove @BananaPhone");
    }

    private void processOp(MessageReceivedEvent e, String[] args) {
        if (args.length < 2) {
            sendMessage(e, ":x: **Improper syntax, no action argument provided!**");
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
                        builder.append(user.getUsername());
                    else
                        builder.append("<@").append(op).append(">");
                    notFirstLoop = true;
                }
                builder.append("]");
                sendMessage(e, builder.toString());
                break;
            default:
                sendMessage(e, new MessageBuilder()
                        .appendString(":x: **Improper syntax, unrecognized argument:** ")
                        .appendString(args[1])
                        .appendString("\n**Provided Command:** ")
                        .appendString(e.getMessage().getContent())
                        .build());
        }
    }

    private void processAddOp(MessageReceivedEvent e, String[] args) {
        if (args.length < 3 || e.getMessage().getMentionedUsers().isEmpty()) {
            sendMessage(e, ":x: Please provide a user!");
            return;
        }

        for (User user : e.getMessage().getMentionedUsers()) {
            try {
                if (Permissions.getPermissions().addOp(user.getId())) {
                    sendMessage(e, ":white_check_mark: Successfully added " + user.getUsername() + " to the OPs list!");
                    return;
                } else {
                    sendMessage(e, user.getUsername() + " is already an OP!");
                    return;
                }
            } catch (Exception e1) {
                sendMessage(e, new MessageBuilder()
                        .appendString(":x: Encountered an error when attempting to add OP.\n")
                        .appendString("User: ").appendString(user.getUsername())
                        .appendString("Error: ").appendString(e1.getClass().getName()).appendString("\n")
                        .appendString("Reason: ").appendString(e1.getMessage())
                        .build());
            }
        }
    }

    private void processRemoveOp(MessageReceivedEvent e, String[] args) {
        if (args.length < 3 || e.getMessage().getMentionedUsers().isEmpty()) {
            sendMessage(e, ":x: Please provide a user!");
            return;
        }
        //Pattern idPattern = Pattern.compile("(?<=<@)[0-9]{18}(?=>)");
        //Matcher idMatch = idPattern.matcher(args[2]);
        //if (!idMatch.find()) {
        //    sendMessage(e, "Sorry, I don't recognize the user provided: " + args[2]);
        //    return;
        //}
        for (User user : e.getMessage().getMentionedUsers()) {
            try {
                if (Permissions.getPermissions().removeOp(user.getId())) {
                    sendMessage(e, ":white_check_mark: Successfully removed " + user.getUsername() + " to the OPs list!");
                    return;
                } else {
                    sendMessage(e, ":x: " + user.getUsername() + " cannot be removed because they weren't an OP!");
                    return;
                }
            } catch (Exception e1) {
                sendMessage(e, new MessageBuilder()
                        .appendString(":x: Encountered an error when attempting to remove OP.\n")
                        .appendString("User: ").appendString(user.getUsername())
                        .appendString("Error: ").appendString(e1.getClass().getName()).appendString("\n")
                        .appendString("Reason: ").appendString(e1.getMessage())
                        .build());
            }
        }
    }
}