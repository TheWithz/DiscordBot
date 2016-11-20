package events;

import bots.RunBot;
import events.commands.ArgParse;
import events.commands.DiscordAsOutputStream;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Created by thewithz on 6/24/16.
 */
public class TerminalHandler extends ListenerAdapter {
    private DiscordAsOutputStream sout = null;
    private TextChannel tc = null;
    private Runtime run = null;
    private String line = "";

    public TerminalHandler() {
        run = Runtime.getRuntime();
    }

    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);
        tc = RunBot.API.getTextChannelById("196041809908989953");
        sout = new DiscordAsOutputStream(tc);
    }

    @Override
    public void onReconnect(ReconnectedEvent event) {
        super.onReconnect(event);
        run = Runtime.getRuntime();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getChannel().equals(tc) && event.getAuthor().getId().equals(RunBot.BOT.getId()))
            return;
        if (!event.getChannel().equals(tc))
            return;

        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(sout));

        ArgParse parser = new ArgParse();
        try {
            Process process = run.exec(parser.parse(event.getMessage().getContent()));
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
                System.out.println(run(br, process).toString());
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        sout.myPrint();

        System.setOut(oldOut);
    }

    private StringBuilder run(BufferedReader br, Process process) throws IOException, InterruptedException {
        StringBuilder msg = new StringBuilder();
        while ((line = br.readLine()) != null) {
            msg.append(line)
               .append("\n");
        }
        process.waitFor();
        msg.append("exit: ")
           .append(process.exitValue());
        process.destroy();
        return msg;
    }
}
