package bots;

import events.AudioHandler;
import events.LoginHandler;
import events.TextChannelHandler;
import events.VoiceChannelHandler;
import events.commands.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.User;

import javax.security.auth.login.LoginException;

public class RunBot {
    public static JDA API = null;
    public static User BOT = null;

    public RunBot() {
        try {
            HelpCommand help = new HelpCommand();
            API = new JDABuilder().setEmail("bot@botbot.com").setPassword("Bot")
                    .addListener(new TextChannelHandler())
                    .addListener(new VoiceChannelHandler())
                    .addListener(new LoginHandler())
                    .addListener(new AudioHandler())
                    .addListener(help.registerCommand(help))
                    .addListener(help.registerCommand(new TranslateCommand()))
                    .addListener(help.registerCommand(new CalculatorCommand()))
                    .addListener(help.registerCommand(new ClearChatCommand()))
                    .addListener(help.registerCommand(new SearchCommand()))
                    .addListener(help.registerCommand(new PermissionsCommand()))
                    .addListener(help.registerCommand(new TodoCommand()))
                    .addListener(help.registerCommand(new RandomNumberCommand())).buildAsync();
        } catch (LoginException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Bot failed to connect");
        }

    }

    public static void main(String[] args) {
        RunBot bot = new RunBot();
    }

}
