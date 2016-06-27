package bots;

import events.LogHandler;
import events.LoginHandler;
import events.TerminalHandler;
import events.commands.*;
import events.commands.generator.*;
import events.commands.music.*;
import misc.Permissions;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
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
    public static final String PREFIX = "$$$";
    public static final String OP_REQUIRED = ":x: Sorry, this command is OP only!";
    public static String OWNER_REQUIRED = null;
    // private static final GitHubClient client = new GitHubClient();

    public RunBot() {
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("Config.json"))));
            //client.setOAuth2Token(obj.getString("gitApiToken")).setCredentials(obj.getString("gitUserName"), obj
            //        .getString("gitPassword"));
            HelpCommand help = new HelpCommand();
            API = new JDABuilder().setBotToken(obj.getString("testBotToken"))
                                  .addListener(new LoginHandler())
                                  .addListener(new LogHandler())
                                  .addListener(new TerminalHandler())
                                  .addListener(help.registerCommand(help, Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new TranslateCommand(), Permissions.Perm.OP_ONLY))
                                  .addListener(help.registerCommand(new CalculatorCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new SearchCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new PermissionsCommand(), Permissions.Perm.OWNER_ONLY))
                                  .addListener(help.registerCommand(new TodoCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new RandomNumberCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new RandomFactCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new BashCommand(), Permissions.Perm.OWNER_ONLY))
                                  .addListener(help.registerCommand(new EvalCommand(), Permissions.Perm.OWNER_ONLY))
                                  .addListener(help.registerCommand(new JoinCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new LeaveCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new PrintQueueCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new LatexCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new StatCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new JsonCommand(), Permissions.Perm.OWNER_ONLY))
                                  .addListener(help.registerCommand(new NowPlayingCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new PauseCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new PlayCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new RepeatCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new ResetCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new RestartCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new ShuffleCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new SkipCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new StopCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new VolumeCommand(), Permissions.Perm.EVERYONE))
                                  .addListener(help.registerCommand(new TagCommand(), Permissions.Perm.EVERYONE))
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
                Files.write(Paths.get("Config.json"), obj.toString(4).getBytes());
                System.out.println("No config file was found. Config.json has been generated, please populate it!");
            } catch (IOException e1) {
                System.out.println("No config file was found and we failed to generate one.");
                e1.printStackTrace();
            }
        }
        TIMER.schedule(new TimerTask() {
            public void run() {
                if (LogHandler.logGrouper.size() >= 8) {
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

    public static void printAsFile(TextChannel channel, StringBuilder b, String fileName) {
        channel.sendTyping();
        BashCommand.runLinuxCommand("touch " + fileName + ".txt");
        File file = new File(fileName + ".txt");
        Path path = Paths.get(fileName + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(b.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        channel.sendFile(file, new MessageBuilder().appendCodeBlock(fileName, "java").build());
        file.delete();
    }

    public static void checkArgs(String[] args, int index, String failMessage, MessageReceivedEvent e) {
        if (args.length < (index + 1)) {
            e.getChannel().sendMessage(failMessage);
            throw new IllegalArgumentException(failMessage);
        }
    }

    public static void checkArgs(String[] args, int index, String failMessage, TextChannel tc) {
        if (args.length < (index + 1)) {
            tc.sendMessage(failMessage);
            throw new IllegalArgumentException(failMessage);
        }
    }

    public static boolean OpRequired(MessageReceivedEvent e) {
        if (!Permissions.getPermissions().isOp(e.getAuthor())) {
            e.getChannel().sendMessage(RunBot.OP_REQUIRED);
            return true;
        }
        return false;
    }

    public static boolean OwnerRequired(MessageReceivedEvent e) {
        if (!e.getAuthor().getId().equals("122764399961309184")) {
            e.getChannel().sendMessage(RunBot.OWNER_REQUIRED);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        RunBot bot = new RunBot();
    }

}
