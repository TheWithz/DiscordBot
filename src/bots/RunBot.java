package bots;

import events.LogHandler;
import events.LoginHandler;
import events.commands.*;
import events.commands.generator.*;
import events.commands.voice.JoinCommand;
import events.commands.voice.LeaveCommand;
import events.commands.voice.MoveCommand;
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
    public static final String prefix = "$$$";

    public RunBot() {
        try {
            HelpCommand help = new HelpCommand();
            API = new JDABuilder().setEmail("bot@botbot.com").setPassword("Bot")
                    .addListener(new LoginHandler())
                    //.addListener(new AudioHandler())
                    .addListener(new LogHandler())
                    .addListener(help.registerCommand(help))
                    .addListener(help.registerCommand(new TranslateCommand()))
                    .addListener(help.registerCommand(new CalculatorCommand()))
                    .addListener(help.registerCommand(new ClearChatCommand()))
                    .addListener(help.registerCommand(new SearchCommand()))
                    .addListener(help.registerCommand(new PermissionsCommand()))
                    .addListener(help.registerCommand(new TodoCommand()))
                    .addListener(help.registerCommand(new RandomNumberCommand()))
                    .addListener(help.registerCommand(new RandomFactCommand()))
                    .addListener(help.registerCommand(new LeaveCommand()))
                    .addListener(help.registerCommand(new JoinCommand()))
                    .addListener(help.registerCommand(new MoveCommand()))
                    .addListener(help.registerCommand(new LinuxCommand()))
                    .addListener(help.registerCommand(new EvalCommand())).buildAsync();
        } catch (LoginException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Bot failed to connect");
        }
        TIMER.schedule(new TimerTask() {
            public void run() {
                //System.out.println("timer was fired. I think.");
            }
        }, 0, 300 * 1000); // runs every 5 minutes
    }

    public static void main(String[] args) {
        RunBot bot = new RunBot();
        //LinuxCommand streamUtil = new LinuxCommand();
    }

}
