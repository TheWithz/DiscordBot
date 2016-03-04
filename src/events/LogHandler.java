package events;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TheWithz on 2/21/16.
 */
public class LogHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter("resources/log.txt", true)))) {
            //{is private?} {yes/no} {is editted?} {yes/no} (time editted) [sender] [who name : ID] | where guild name : ID | -> | where channel name : ID | (time sent) message
            out.printf("");
/// TODO: 2/21/16 format log messages based on things like when it was editted and such
        } catch (IOException error) {
            System.out.println("File not written to text file");
        }
    }

    private static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }


}
