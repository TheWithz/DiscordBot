package events.commands;

import bots.RunBot;
import misc.Permissions;
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
        runLinuxCommand(command.toString().substring(RunBot.PREFIX.length(), command.lastIndexOf(" ")));
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "ping",
                             RunBot.PREFIX + "ls",
                             RunBot.PREFIX + "curl",
                             RunBot.PREFIX + "nmap",
                             RunBot.PREFIX + "cat",
                             RunBot.PREFIX + "tar",
                             RunBot.PREFIX + "touch",
                             RunBot.PREFIX + "crunch",
                             RunBot.PREFIX + "man",
                             RunBot.PREFIX + "grep",
                             RunBot.PREFIX + "mkdir",
                             RunBot.PREFIX + "mv",
                             RunBot.PREFIX + "stat",
                             RunBot.PREFIX + "python3",
                             RunBot.PREFIX + "cd",
                             RunBot.PREFIX + "echo",
                             RunBot.PREFIX + "cp");
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

    public static StringBuilder runLinuxCommand(String com) {
        try {
            process = Runtime.getRuntime().exec(com);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return run(br);
        } catch (Exception e) {
            return new StringBuilder(e.getMessage());
        }
    }

    private static StringBuilder run(BufferedReader br) throws IOException, InterruptedException {
        StringBuilder msg = new StringBuilder();
        while ((line = br.readLine()) != null) {
            msg.append(line);
            msg.append("\n");
        }
        process.waitFor();
        msg.append("exit: ")
           .append(process.exitValue());
        process.destroy();
        return msg;
    }
}
