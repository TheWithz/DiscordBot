package events.commands;

import bots.RunBot;
import misc.Permissions;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 2/26/16.
 */
public class LinuxCommand extends Command {

    private static String line = "";
    public static Process process;

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (!Permissions.getPermissions().isOp(e.getAuthor())) {
            e.getChannel().sendMessage("Sorry, this command is OP only!");
            return;
        }

        StringBuilder command = new StringBuilder();
        Arrays.asList(args).forEach(string -> {
            command.append(string);
            command.append(" ");
        });
        runLinuxCommand(e, command.toString().substring(RunBot.prefix.length(), command.lastIndexOf(" ")));
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "ping",
                RunBot.prefix + "ls",
                RunBot.prefix + "curl",
                RunBot.prefix + "nmap",
                RunBot.prefix + "cat",
                RunBot.prefix + "tar",
                RunBot.prefix + "touch",
                RunBot.prefix + "crunch",
                RunBot.prefix + "man",
                RunBot.prefix + "grep",
                RunBot.prefix + "mkdir",
                RunBot.prefix + "mv",
                RunBot.prefix + "stat",
                RunBot.prefix + "python3.5",
                RunBot.prefix + "python",
                RunBot.prefix + "cd",
                RunBot.prefix + "echo",
                RunBot.prefix + "cp");
    }

    // TODO: 2/27/16 fill out override methods
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getUsageInstructions() {
        return null;
    }

    public static void runLinuxCommand(MessageReceivedEvent event, String com) {
        try {
            process = Runtime.getRuntime().exec(com);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder b = run(br);
            if (b.length() > 2000) {
                RunBot.printAsFile(event, b, com);
            } else {
                if (com.split("\\s+")[1].equals("resources/html.py")) {
                    event.getChannel().sendMessage(new MessageBuilder().appendString(b.toString()).build());
                } else
                    System.out.println(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder run(BufferedReader br) throws IOException, InterruptedException {
        StringBuilder msg = new StringBuilder();
        while ((line = br.readLine()) != null) {
            msg.append(line);
            msg.append("\n");
        }
        process.waitFor();
        msg.append("exit: " + process.exitValue());
        process.destroy();
        return msg;
    }
}
