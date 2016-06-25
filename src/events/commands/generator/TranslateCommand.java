package events.commands.generator;

import bots.RunBot;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

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
        if (RunBot.OpRequired(e))
            return;
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
    public List<String> getUsageInstructionsEveryone() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return Collections.singletonList(RunBot.PREFIX + "tran <Original Language> <Translated Language> <Message>");
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return getUsageInstructionsOp();
    }

    private void generateTranslatedText(MessageReceivedEvent e, String[] commandArguments) {
        RunBot.checkArgs(commandArguments, 2, ":x: No language was specified to translate from. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);
        RunBot.checkArgs(commandArguments, 3, ":x: No language was specified to translate to. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);
        RunBot.checkArgs(commandArguments, 4, ":x: No Content was specified to translate. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);

        //Set your Windows Azure Marketplace client info - See http:msdn.microsoft.com/en-us/library/hh454950.aspx
        Translate.setClientId(MICROSOFT_CLIENT_ID);
        Translate.setClientSecret(MICROSOFT_CLIENT_SECRET);
        String translatedText = null;
        try {
            translatedText = Translate.execute(StringUtils.join(commandArguments, " ", 4, commandArguments.length), Language.valueOf(commandArguments[1].toUpperCase()), Language
                    .valueOf(commandArguments[2].toUpperCase()));
        } catch (Exception e1) {
            sendMessage(e, ":x: " + e1.getMessage());
        }
        if (translatedText != null)
            e.getChannel().sendMessage(":white_check_mark: " + translatedText);
    }

}
