package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

import static events.commands.music.AudioUtil.manager;

/**
 * Created by TheWithz on 4/24/16.
 */
public class JoinCommand extends Command {
    //Start an audio connection with a VoiceChannel
    String prevChannel = null;

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 1, ":x: No Channel was specified to join. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);
        //Separates the name of the channel so that we can search for it
        String chanName = args[1];

        //Scans through the VoiceChannels in this Guild, looking for one with a case-insensitive matching name.
        VoiceChannel channel = event.getGuild().getVoiceChannels().stream().filter(
                vChan -> vChan.getName().equalsIgnoreCase(chanName))
                                    .findFirst().orElse(null);  //If there isn't a matching name, return null.
        if (channel == null) {
            event.getChannel().sendMessageAsync(":x: There isn't a VoiceChannel in this Guild with the name: '" + chanName + "'", null);
            return;
        }
        if (manager != null && manager.isConnected()) {
            String prevGuild = AudioUtil.manager.getGuild().getName();
            AudioUtil.setManagerAndPlayer(event);
            AudioUtil.manager.moveAudioConnection(channel);
            event.getChannel()
                 .sendMessageAsync(String.format(":white_check_mark: Successfully moved from **{**`%1$s`**}**`/`**(**`%2$s`**)** to **{**`%3$s`**}**`/`**(**`%4$s`**)**",
                                                 prevGuild,
                                                 prevChannel,
                                                 event.getGuild().getName(),
                                                 chanName), null);
            return;
        }
        AudioUtil.setManagerAndPlayer(event);
        manager.openAudioConnection(channel);
        prevChannel = chanName;
        event.getChannel()
             .sendMessageAsync(String.format(":white_check_mark: Successfully joined **{**`%1$s`**}**`/`**(**`%2$s`**)**",
                                             event.getGuild().getName(),
                                             chanName), null);
    }

    @Override
    public java.util.List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "join");
    }

    @Override
    public String getDescription() {
        return "Creates an audio connection with the channel of your choosing.";
    }

    @Override
    public String getName() {
        return "Join Command";
    }

    @Override
    public java.util.List<String> getUsageInstructionsEveryone() {
        return Collections.singletonList(String.format("(%1$s) <Audio Channel>\n" +
                                                               "[Example:](%1$s) <General> This will have <%2$s> join the General Audio Channel", getAliases().get(0), RunBot.BOT.getUsername()));
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

