package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/24/16.
 */
public class PrintQueueCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
//        if (AudioUtil.player == null) {
//            event.getChannel().sendMessage(":x: Cannot show the queue of a playlist that hasn't been created yet!").queue();
//            return;
//        }
//        if (args.length == 1) {
//            java.util.List<AudioSource> queue = AudioUtil.player.getAudioQueue();
//            if (queue.isEmpty()) {
//                event.getChannel().sendMessage(":x: The queue is currently empty!").queue();
//                return;
//            }
//
//            MessageBuilder builder = new MessageBuilder();
//            builder.appendString(":white_check_mark: __Current Queue.  Entries: " + queue.size() + "__\n");
//            for (int i = 0; i < queue.size() && i < 10; i++) {
//                AudioInfo info = queue.get(i).getInfo();
//                // builder.appendString("**(" + (i + 1) + ")** ");
//                if (info == null)
//                    builder.appendString(":x: *Could not get info for this song.*");
//                else {
//                    AudioTimestamp duration = info.getDuration();
//                    builder.appendString("`[");
//                    if (duration == null)
//                        builder.appendString("N/A");
//                    else
//                        builder.appendString(duration.getTimestamp());
//                    builder.appendString("]` " + info.getTitle() + "\n");
//                }
//            }
//
//            boolean error = false;
//            int totalSeconds = 0;
//            for (AudioSource source : queue) {
//                AudioInfo info = source.getInfo();
//                if (info == null || info.getDuration() == null) {
//                    error = true;
//                    continue;
//                }
//                totalSeconds += info.getDuration().getTotalSeconds();
//            }
//
//            builder.appendString("\n:white_check_mark: Total Queue Time Length: " + AudioTimestamp.fromSeconds(totalSeconds).getTimestamp());
//            if (error)
//                builder.appendString(":x: `An error occured calculating total time. Might not be completely valid.`");
//            event.getChannel().sendMessage(builder.build()).queue();
//        } else if (args.length == 2 && args[1].equals("playlists")) {
//            try {
//                StringBuilder builder = new StringBuilder();
//                JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Playlists.json"))));
//                obj.keySet().forEach(jsonKey -> builder.append("[ **")
//                                                       .append(obj.getString(jsonKey))
//                                                       .append("** ]")
//                                                       .append('\n')
//                                                       .append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"));
//                event.getChannel().sendMessage(builder.toString()).queue();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public java.util.List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "printQueue", RunBot.PREFIX + "listQueue", RunBot.PREFIX + "queue");
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
    public java.util.List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s)] \n" +
                                                               "[Example:](%1$s) Shows information for songs currently in <%2$s's> queue.",
                                                       getAliases().get(0),
                                                       RunBot.BOT.getName()));
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return getUsageInstructionsEveryone();
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return getUsageInstructionsEveryone();
    }
}
