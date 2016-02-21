package bots;

import events.*;
import events.commands.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.User;

import javax.security.auth.login.LoginException;
import java.util.Timer;
import java.util.TimerTask;

public class RunBot {
    public static JDA API = null;
    public static User BOT = null;
    private static final Timer TIMER = new Timer();

    public RunBot() {
        try {
            HelpCommand help = new HelpCommand();
            API = new JDABuilder().setEmail("bot@botbot.com").setPassword("Bot")
                    .addListener(new TextChannelHandler())
                    .addListener(new VoiceChannelHandler())
                    .addListener(new LoginHandler())
                    .addListener(new AudioHandler())
                    .addListener(new LogHandler())
                    .addListener(help.registerCommand(help))
                    .addListener(help.registerCommand(new TranslateCommand()))
                    .addListener(help.registerCommand(new CalculatorCommand()))
                    .addListener(help.registerCommand(new ClearChatCommand()))
                    .addListener(help.registerCommand(new SearchCommand()))
                    .addListener(help.registerCommand(new PermissionsCommand()))
                    .addListener(help.registerCommand(new TodoCommand()))
                    .addListener(help.registerCommand(new RandomNumberCommand()))
                    .addListener(help.registerCommand(new RandomFactCommand())).buildAsync();
        } catch (LoginException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Bot failed to connect");
        }
        TIMER.schedule(new TimerTask() {
            public void run() {

            }
        }, 0, 300 * 1000); // runs every 5 minutes
    }

    public static void main(String[] args) {
        RunBot bot = new RunBot();
    }

}
