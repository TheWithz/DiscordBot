package events;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

/**
 * Created by TheWithz on 2/21/16.
 */
public class LogHandler extends ListenerAdapter {
    private boolean enabled = true;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter("resources/log.txt", true)))) {
            if (!enabled) return;

            String content = event.getMessage().getContent();
            String message = (content.contains("\n")) ? "\n" + content : content;
            out.printf("[%s] [%s#%s] %s: %s\n",
                    event.getMessage().getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                    event.getGuild().getName(),
                    event.getChannel().toString(),
                    event.getAuthor().getUsername(),
                    message);

        } catch (IOException error) {
            System.out.println("File not written to text file");
        }
    }

}
