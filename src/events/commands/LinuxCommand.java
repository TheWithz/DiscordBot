package events.commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.io.*;
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
        StringBuilder command = new StringBuilder();
        Arrays.asList(args).forEach(string -> {
            command.append(string);
            command.append(" ");
        });
        runLinuxCommand(command.toString().substring(1, command.lastIndexOf(" ")));
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("$ping", "$ls");
    }

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

    public static void runLinuxCommand(String com) {
        try {
            process = Runtime.getRuntime().exec(com);
            //Thread t = new DirectedStream(process.getInputStream(), System.out);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            // if (command[0].equalsIgnoreCase("ls")) {
            System.out.println(run(br));
            //}
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
