package events.commands.generator;

import events.commands.Command;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/21/16.
 */
public class HelpCommand extends Command {
    private static final String NO_NAME = "No name provided for this Command. Sorry!";
    private static final String NO_DESCRIPTION = "No description has been provided for this Command. Sorry!";
    private static final String NO_USAGE = "No usage instructions have been provided for this Command. Sorry!";

    private ArrayList<Command> Commands;

    public HelpCommand() {
        Commands = new ArrayList<Command>();
    }

    public Command registerCommand(Command Command) {
        Commands.add(Command);
        return Command;
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (!e.isPrivate()) {
            e.getTextChannel().sendMessage(new MessageBuilder()
                    .appendMention(e.getAuthor())
                    .appendString(": Check your PMs Please.")
                    .build());
        }
        sendPrivate(e.getAuthor().getPrivateChannel(), args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("$help", "$commands");
    }

    @Override
    public String getDescription() {
        return "Command that helps use all other Commands!";
    }

    @Override
    public String getName() {
        return "Help Command";
    }

    @Override
    public String getUsageInstructions() {
        return "$help   **OR**  $help *<Command>*\n"
                + "$help - returns the list of Commands along with a simple description of each.\n"
                + "$help <Command> - returns the name, description, aliases and usage information of a Command.\n"
                + "   - This can use the aliases of a Command as input as well.\n"
                + "__Example:__ $help clch";
    }

    private void sendPrivate(PrivateChannel channel, String[] args) {
        if (args.length < 2) {
            StringBuilder s = new StringBuilder();
            for (Command c : Commands) {
                String description = c.getDescription();
                description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

                s.append("**").append(c.getAliases().get(0)).append("** - ");
                s.append(description).append("\n");
            }

            channel.sendMessage(new MessageBuilder()
                    .appendString("The following Commands are supported by the bot\n")
                    .appendString(s.toString())
                    .build());
        } else {
            String Command = args[1].charAt(0) == '$' ? args[1] : "$" + args[1];    //If there is not a preceding . attached to the Command we are search, then prepend one.
            for (Command c : Commands) {
                if (c.getAliases().contains(Command)) {
                    String name = c.getName();
                    String description = c.getDescription();
                    String usageInstructions = c.getUsageInstructions();
                    name = (name == null || name.isEmpty()) ? NO_NAME : name;
                    description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;
                    usageInstructions = (usageInstructions == null || usageInstructions.isEmpty()) ? NO_USAGE : usageInstructions;

                    //TODO: Replace with a PrivateMessage
                    channel.sendMessage(new MessageBuilder()
                            .appendString("**Name:** " + name + "\n")
                            .appendString("**Description:** " + description + "\n")
                            .appendString("**Alliases:** " + StringUtils.join(c.getAliases(), ", ") + "\n")
                            .appendString("**Usage:** ")
                            .appendString(usageInstructions)
                            .build());
                    return;
                }
            }
            channel.sendMessage(new MessageBuilder()
                    .appendString("The provided Command '**" + args[1] + "**' does not exist. Use .help to list all Commands.")
                    .build());
        }
    }
}
