package events.commands;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * Created by TheWithz on 2/15/16.
 */
public class TranslateCommand extends ListenerAdapter {
    private final String MICROSOFT_CLIENT_ID = "DiscordBotForTheWithz";
    private final String MICROSOFT_CLIENT_SECRET = "SDy+DFjPKIzmwkC59aA1E4tyIoTn4nAoWKhCEEfOksk=";

    public TranslateCommand() {

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith("$tran "))
            generateTranslatedText(event.getMessage().getContent().split("\\s+"));
    }

    private void generateTranslatedText(String[] commandArguments) {
        if (commandArguments.length < 4) {
            System.out.println("Your syntax for this command is incorrect");
            return;
        }
        //Set your Windows Azure Marketplace client info - See http:msdn.microsoft.com/en-us/library/hh454950.aspx
        Translate.setClientId(MICROSOFT_CLIENT_ID);
        Translate.setClientSecret(MICROSOFT_CLIENT_SECRET);
        try {
            StringBuilder message = new StringBuilder();
            for (int i = 3; i < commandArguments.length; i++) {
                message.append(commandArguments[i] + " ");
            }
            String translatedText = Translate.execute(message.toString(), Language.valueOf(commandArguments[1].toUpperCase()), Language.valueOf(commandArguments[2].toUpperCase()));
            System.out.println(translatedText);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

}
