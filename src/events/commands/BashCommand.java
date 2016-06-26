package events.commands;

import bots.RunBot;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 2/26/16.
 */
public class BashCommand extends Command {

    private static String outLine = "";
    private static String errLine = "";
    public static Process process;

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (RunBot.OwnerRequired(e))
            return;

        sendMessage(e, runLinuxCommand(StringUtils.join(args, " ", 1, args.length)).toString());
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "bash");
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
    public List<String> getUsageInstructionsEveryone() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return null;
    }

    public static StringBuilder runLinuxCommand(String com) {
        try {
            process = Runtime.getRuntime().exec(com);
            BufferedReader brOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader brErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            return run(brOut, brErr);
        } catch (Exception e) {
            return new StringBuilder(e.getMessage());
        }
    }

    private static StringBuilder run(BufferedReader brOut, BufferedReader brErr) throws IOException, InterruptedException {
        StringBuilder msgOut = new StringBuilder();
        while ((outLine = brOut.readLine()) != null) {
            msgOut.append(outLine);
            msgOut.append("\n");
        }
        StringBuilder msgErr = new StringBuilder();
        while ((errLine = brErr.readLine()) != null) {
            msgErr.append(errLine);
            msgErr.append("\n");
        }
        process.waitFor();
        msgErr.append("exit: ")
              .append(process.exitValue());
        process.destroy();
        StringBuilder finalOut = new StringBuilder();
        return finalOut.append("```fix\nOutPut:```").append(msgOut.toString()).append("```fix\nError:```").append(msgErr.toString());
    }
}
