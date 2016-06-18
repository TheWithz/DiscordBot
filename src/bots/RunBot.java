package bots;

import events.LogHandler;
import events.LoginHandler;
import events.commands.*;
import events.commands.generator.*;
import events.commands.music.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RunBot {
    public static JDA API = null;
    public static User BOT = null;
    public static TextChannel LOG = null;
    private static final Timer TIMER = new Timer();
    public static final String prefix = "$$$";
    private static final GitHubClient client = new GitHubClient();

    public RunBot() {
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Config.json"))));
            client.setOAuth2Token(obj.getString("gitApiToken")).setCredentials(obj.getString("gitUserName"), obj
                    .getString("gitPassword"));
            HelpCommand help = new HelpCommand();
            API = new JDABuilder().setBotToken(obj.getString("testBotToken"))
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
                                  .addListener(help.registerCommand(new PrintQueueCommand()))
                                  .addListener(help.registerCommand(new LatexCommand()))
                                  .addListener(help.registerCommand(new StatCommand()))
                                  .addListener(help.registerCommand(new SaveCommand()))
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
                                  .setBulkDeleteSplittingEnabled(false)
                                  .buildAsync();
        } catch (IllegalArgumentException e) {
            System.out.println("The config was not populated. Please provide a token.");
        } catch (LoginException e) {
            System.out.println("The provided botToken was incorrect. Please provide valid details.");
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/ catch (JSONException e) {
            System.err.println("Encountered a JSON error. Most likely caused due to an outdated or ill-formatted config.\n" +
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
                if (LogHandler.logGrouper.size() >= 10) {
                    StringBuilder builder = new StringBuilder();
                    LogHandler.logGrouper.forEach(builder::append);
                    RunBot.LOG.sendMessage(builder.toString());
                    LogHandler.logGrouper.clear();
                }
            }
        }, 0, 3 * 1000); // runs every 5 seconds *i think*
    }

    public static String getUptime() {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long uptime = rb.getUptime();

        long days = TimeUnit.MILLISECONDS.toDays(uptime);
        uptime -= TimeUnit.DAYS.toMillis(days);

        long hours = TimeUnit.MILLISECONDS.toHours(uptime);
        uptime -= TimeUnit.HOURS.toMillis(hours);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime);
        uptime -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime);

        return "**Uptime:** " + days + (days != 1L ? " days " : " day ") +
                hours + (hours != 1L ? " hours " : " hour ") +
                minutes + (minutes != 1L ? " minutes " : " minute ") +
                seconds + (seconds != 1L ? " seconds " : " second");
    }

    public static void printAsFile(MessageReceivedEvent event, StringBuilder b, String fileName) {
        event.getChannel().sendTyping();
        LinuxCommand.runLinuxCommand(event, "touch resources/" + fileName + ".txt");
        File file = new File("resources/" + fileName + ".txt");
        Path path = Paths.get("resources/" + fileName + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(b.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        event.getChannel().sendFile(file, new MessageBuilder().appendCodeBlock(fileName, "java").build());
        file.delete();
    }

    public static void printAsFile(TextChannel channel, StringBuilder b, String fileName) {
        channel.sendTyping();
        LinuxCommand.runLinuxCommand(channel, "touch resources/" + fileName + ".txt");
        File file = new File("resources/" + fileName + ".txt");
        Path path = Paths.get("resources/" + fileName + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(b.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        channel.sendFile(file, new MessageBuilder().appendCodeBlock(fileName, "java").build());
        file.delete();
    }

    public static void main(String[] args) {
        RunBot bot = new RunBot();
    }

}
