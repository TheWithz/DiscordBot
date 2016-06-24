package events.commands.generator;

import bots.RunBot;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import events.commands.Command;
import misc.Permissions;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/15/16.
 */
public class TranslateCommand extends Command {
    private final String MICROSOFT_CLIENT_ID = "DiscordBotForTheWithz";
    private final String MICROSOFT_CLIENT_SECRET = "SDy+DFjPKIzmwkC59aA1E4tyIoTn4nAoWKhCEEfOksk=";

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (!Permissions.getPermissions().isOp(e.getAuthor())) {
            e.getChannel().sendMessage("Sorry, this command is OP only");
            return;
        }
        generateTranslatedText(e, args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "tran", RunBot.PREFIX + "translate");
    }

    @Override
    public String getDescription() {
        return "Command that translates a message!";
    }

    @Override
    public String getName() {
        return "Translate Command";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(RunBot.PREFIX + "tran <Original Language> <Translated Language> <Message>");
    }

    private void generateTranslatedText(MessageReceivedEvent e, String[] commandArguments) {
        if (commandArguments.length < 4) {
            e.getChannel().sendMessage("Your syntax for this command is incorrect");
            e.getChannel().sendMessage("**" + RunBot.PREFIX + "help tran**");
            return;
        }
        //Set your Windows Azure Marketplace client info - See http:msdn.microsoft.com/en-us/library/hh454950.aspx
        Translate.setClientId(MICROSOFT_CLIENT_ID);
        Translate.setClientSecret(MICROSOFT_CLIENT_SECRET);
        try {
            StringBuilder message = new StringBuilder();
            for (int i = 3; i < commandArguments.length; i++) {
                message.append(commandArguments[i]).append(" ");
            }
            String translatedText = Translate.execute(message.toString(), Language.valueOf(commandArguments[1].toUpperCase()), Language.valueOf(commandArguments[2].toUpperCase()));
            e.getChannel().sendMessage(translatedText);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

}
