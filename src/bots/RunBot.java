package bots;

import events.LogHandler;
import events.LoginHandler;
import events.commands.*;
import events.commands.generator.*;
import events.commands.music.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class RunBot {
    public static JDA API = null;
    public static User BOT = null;
    public static HashMap<String, HashSet<User>> prefixes;
    private static final Timer TIMER = new Timer();
    public static final String prefix = "$$$";

    public RunBot() {
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Config.json"))));
            HelpCommand help = new HelpCommand();
            API = new JDABuilder().setBotToken(obj.getString("botToken"))
                    .addListener(new LoginHandler())
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
                    .addListener(help.registerCommand(new LinuxCommand()))
                    .addListener(help.registerCommand(new EvalCommand()))
                    .addListener(help.registerCommand(new JoinCommand()))
                    .addListener(help.registerCommand(new LeaveCommand()))
                    .addListener(help.registerCommand(new ListCommand()))
                    .addListener(help.registerCommand(new NowPlayingCommand()))
                    .addListener(help.registerCommand(new PauseCommand()))
                    .addListener(help.registerCommand(new PlayCommand()))
                    .addListener(help.registerCommand(new RepeatCommand()))
                    .addListener(help.registerCommand(new ResetCommand()))
                    .addListener(help.registerCommand(new RestartCommand()))
                    .addListener(help.registerCommand(new ShuffleCommand()))
                    .addListener(help.registerCommand(new SkipCommand()))
                    .addListener(help.registerCommand(new StopCommand()))
                    .addListener(help.registerCommand(new VolumeCommand()))
                    .buildAsync();
        } catch (IllegalArgumentException e) {
            System.out.println("The config was not populated. Please provide a token.");
        } catch (LoginException e) {
            System.out.println("The provided botToken was incorrect. Please provide valid details.");
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/ catch (JSONException e) {
            System.err.println("Encountered a JSON error. Most likely caused due to an outdated or ill-formated config.\n" +
                    "Please delete the config so that it can be regenerated. JSON Error:\n");
            e.printStackTrace();
        } catch (IOException e) {
            JSONObject obj = new JSONObject();
            obj.put("botToken", "");
            try {
                Files.write(Paths.get("resources/Config.json"), obj.toString(4).getBytes());
                System.out.println("No config file was found. Config.json has been generated, please populate it!");
            } catch (IOException e1) {
                System.out.println("No config file was found and we failed to generate one.");
                e1.printStackTrace();
            }
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
