package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import net.dv8tion.jda.player.source.AudioTimestamp;

import java.util.Arrays;

/**
 * Created by TheWithz on 4/24/16.
 */
public class ListCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
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
            builder.appendString("`An error occured calculating total time. Might not be completely valid.");
        event.getChannel().sendMessage(builder.build());
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "list");
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
    public java.util.List<String> getUsageInstructions() {
        return null;
    }
}
