package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import net.dv8tion.jda.player.source.AudioTimestamp;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by TheWithz on 4/24/16.
 */
public class PrintQueueCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (args.length == 1) {
            java.util.List<AudioSource> queue = AudioUtil.player.getAudioQueue();
            if (queue.isEmpty()) {
                event.getChannel().sendMessage("The queue is currently empty!");
                return;
            }

            MessageBuilder builder = new MessageBuilder();
            builder.appendString("__Current Queue.  Entries: " + queue.size() + "__\n");
            for (int i = 0; i < queue.size() && i < 10; i++) {
                AudioInfo info = queue.get(i).getInfo();
                // builder.appendString("**(" + (i + 1) + ")** ");
                if (info == null)
                    builder.appendString("*Could not get info for this song.*");
                else {
                    AudioTimestamp duration = info.getDuration();
                    builder.appendString("`[");
                    if (duration == null)
                        builder.appendString("N/A");
                    else
                        builder.appendString(duration.getTimestamp());
                    builder.appendString("]` " + info.getTitle() + "\n");
                }
            }

            boolean error = false;
            int totalSeconds = 0;
            for (AudioSource source : queue) {
                AudioInfo info = source.getInfo();
                if (info == null || info.getDuration() == null) {
                    error = true;
                    continue;
                }
                totalSeconds += info.getDuration().getTotalSeconds();
            }

            builder.appendString("\nTotal Queue Time Length: " + AudioTimestamp.fromSeconds(totalSeconds).getTimestamp());
            if (error)
                builder.appendString("`An error occured calculating total time. Might not be completely valid.`");
            event.getChannel().sendMessage(builder.build());
        } else if (args.length == 2 && args[1].equals("playlists")) {
            try {
                StringBuilder builder = new StringBuilder();
                JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Playlists.json"))));
                obj.keySet().stream().forEach(jsonKey -> {
                    builder.append("[ **");
                    builder.append(obj.getString(jsonKey));
                    builder.append("** ]");
                    builder.append('\n');
                    builder.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                });
                event.getChannel().sendMessage(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "printQueue", RunBot.PREFIX + "listQueue");
    }

    @Override
    public String getDescription() {
        return "Prints out the list of songs left in the queue";
    }

    @Override
    public String getName() {
        return "PrintQueue Command";
    }

    @Override
    public java.util.List<String> getUsageInstructions() {
        return Arrays.asList(RunBot.PREFIX + "printQueue");
    }
}
