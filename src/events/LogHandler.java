package events;

import bots.RunBot;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.audio.*;
import net.dv8tion.jda.events.channel.text.*;
import net.dv8tion.jda.events.channel.voice.*;
import net.dv8tion.jda.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.events.guild.GuildUpdateEvent;
import net.dv8tion.jda.events.guild.member.*;
import net.dv8tion.jda.events.guild.role.*;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageEmbedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.events.user.UserGameUpdateEvent;
import net.dv8tion.jda.events.user.UserNameUpdateEvent;
import net.dv8tion.jda.events.user.UserOnlineStatusUpdateEvent;
import net.dv8tion.jda.events.voice.*;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ofPattern;

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
            if (!event.isPrivate()) {
                out.printf("[%s] [%s#%s] %s: %s\n",
                           event.getMessage().getTime().format(ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                           event.getGuild().getName(),
                           event.getChannel().toString(),
                           event.getAuthor().getUsername(),
                           message);
            } else {
                out.printf("[%s] [%s#%s] %s: %s\n",
                           event.getMessage().getTime().format(ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                           event.getPrivateChannel().getUser().getAsMention(),
                           event.getChannel().toString(),
                           event.getAuthor().getUsername(),
                           message);
            }

        } catch (IOException error) {
            System.out.println("Message not written to text file");
        }
    }

    @Override
    public void onUserNameUpdate(UserNameUpdateEvent event) {
        super.onUserNameUpdate(event);
    }

    @Override
    public void onUserOnlineStatusUpdate(UserOnlineStatusUpdateEvent event) {
        super.onUserOnlineStatusUpdate(event);
    }

    @Override
    public void onUserGameUpdate(UserGameUpdateEvent event) {
        super.onUserGameUpdate(event);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        super.onGuildMessageUpdate(event);
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        super.onGuildMessageDelete(event);
    }

    @Override
    public void onGuildMessageEmbed(GuildMessageEmbedEvent event) {
        super.onGuildMessageEmbed(event);
    }

    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        super.onTextChannelDelete(event);
    }

    @Override
    public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
        super.onTextChannelUpdateName(event);
    }

    @Override
    public void onTextChannelUpdateTopic(TextChannelUpdateTopicEvent event) {
        super.onTextChannelUpdateTopic(event);
    }

    @Override
    public void onTextChannelUpdatePosition(TextChannelUpdatePositionEvent event) {
        super.onTextChannelUpdatePosition(event);
    }

    @Override
    public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event) {
        super.onTextChannelUpdatePermissions(event);
    }

    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        super.onTextChannelCreate(event);
    }

    @Override
    public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {
        super.onVoiceChannelUpdateName(event);
    }

    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        super.onVoiceChannelDelete(event);
    }

    @Override
    public void onVoiceChannelUpdatePosition(VoiceChannelUpdatePositionEvent event) {
        super.onVoiceChannelUpdatePosition(event);
    }

    @Override
    public void onVoiceChannelUpdateUserLimit(VoiceChannelUpdateUserLimitEvent event) {
        super.onVoiceChannelUpdateUserLimit(event);
    }

    @Override
    public void onVoiceChannelUpdateBitrate(VoiceChannelUpdateBitrateEvent event) {
        super.onVoiceChannelUpdateBitrate(event);
    }

    @Override
    public void onVoiceChannelUpdatePermissions(VoiceChannelUpdatePermissionsEvent event) {
        super.onVoiceChannelUpdatePermissions(event);
    }

    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
        super.onVoiceChannelCreate(event);
    }

    @Override
    public void onGuildUpdate(GuildUpdateEvent event) {
        super.onGuildUpdate(event);
    }

    @Override
    public void onGuildAvailable(GuildAvailableEvent event) {
        super.onGuildAvailable(event);
    }

    @Override
    public void onGuildUnavailable(GuildUnavailableEvent event) {
        super.onGuildUnavailable(event);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);
        RunBot.LOG.sendMessage(new MessageBuilder().appendCodeBlock("[" + getCurrentTime() + "]" +
                                                                            "[" + event.getUser().getUsername() +
                                                                            "](ID:" + event.getUser().getId() +
                                                                            ") Joined Guild [" + event.getGuild() +
                                                                            "]", "md")
                                                   .build());
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        super.onGuildMemberLeave(event);
        RunBot.LOG.sendMessage(new MessageBuilder().appendCodeBlock("[" + getCurrentTime() + "]" +
                                                                            "[" + event.getUser().getUsername() +
                                                                            "](ID:" + event.getUser().getId() +
                                                                            ") Left Guild [" + event.getGuild() +
                                                                            "]", "md")
                                                   .build());
    }

    @Override
    public void onGuildMemberBan(GuildMemberBanEvent event) {
        super.onGuildMemberBan(event);
    }

    @Override
    public void onGuildMemberUnban(GuildMemberUnbanEvent event) {
        super.onGuildMemberUnban(event);
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        super.onGuildMemberRoleAdd(event);
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        super.onGuildMemberRoleRemove(event);
    }

    @Override
    public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
        super.onGuildMemberNickChange(event);
        String name = event.getUser().getUsername();
        String oldNick = event.getPrevNick() == null ? name : event.getPrevNick();
        String newNick = event.getNewNick() == null ? name : event.getNewNick();
        RunBot.LOG.sendMessage(new MessageBuilder().appendCodeBlock("[" + getCurrentTime() + "]" +
                                                                            "[" + name +
                                                                            "](ID:" + event.getUser().getId() +
                                                                            ") Changed their nickname on Guild " +
                                                                            "[" + event.getGuild()
                                                                                       .getName() + "] from/to " +
                                                                            "[" + oldNick + "](" +
                                                                            newNick + ")", "md")
                                                   .build());
    }

    @Override
    public void onGuildRoleCreate(GuildRoleCreateEvent event) {
        super.onGuildRoleCreate(event);
    }

    @Override
    public void onGuildRoleDelete(GuildRoleDeleteEvent event) {
        super.onGuildRoleDelete(event);
    }

    @Override
    public void onGuildRoleUpdate(GuildRoleUpdateEvent event) {
        super.onGuildRoleUpdate(event);
    }

    @Override
    public void onGuildRoleUpdateName(GuildRoleUpdateNameEvent event) {
        super.onGuildRoleUpdateName(event);
    }

    @Override
    public void onGuildRoleUpdateColor(GuildRoleUpdateColorEvent event) {
        super.onGuildRoleUpdateColor(event);
    }

    @Override
    public void onGuildRoleUpdatePosition(GuildRoleUpdatePositionEvent event) {
        super.onGuildRoleUpdatePosition(event);
    }

    @Override
    public void onGuildRoleUpdatePermission(GuildRoleUpdatePermissionEvent event) {
        super.onGuildRoleUpdatePermission(event);
    }

    @Override
    public void onGuildRoleUpdateGrouped(GuildRoleUpdateGroupedEvent event) {
        super.onGuildRoleUpdateGrouped(event);
    }

    @Override
    public void onVoiceSelfMute(VoiceSelfMuteEvent event) {
        super.onVoiceSelfMute(event);
    }

    @Override
    public void onVoiceSelfDeaf(VoiceSelfDeafEvent event) {
        super.onVoiceSelfDeaf(event);
    }

    @Override
    public void onVoiceServerMute(VoiceServerMuteEvent event) {
        super.onVoiceServerMute(event);
    }

    @Override
    public void onVoiceServerDeaf(VoiceServerDeafEvent event) {
        super.onVoiceServerDeaf(event);
    }

    @Override
    public void onVoiceMute(VoiceMuteEvent event) {
        super.onVoiceMute(event);
    }

    @Override
    public void onVoiceDeaf(VoiceDeafEvent event) {
        super.onVoiceDeaf(event);
    }

    @Override
    public void onVoiceJoin(VoiceJoinEvent event) {
        super.onVoiceJoin(event);
        // if they entered the AFK channel
        if (RunBot.API.getVoiceChannelByName("AFK").contains(event.getChannel())) {
            RunBot.LOG.sendMessage(new MessageBuilder().appendCodeBlock("[" + getCurrentTime() + "]" +
                                                                                "[" + event.getUser().getUsername() +
                                                                                "](ID:" + event.getUser().getId() +
                                                                                ") Has gone AFK", "md")
                                                       .build());
        } else {

        }
    }

    @Override
    public void onVoiceLeave(VoiceLeaveEvent event) {
        super.onVoiceLeave(event);
        // if they left the AFK channel
        if (RunBot.API.getVoiceChannelByName("AFK").contains(event.getOldChannel())) {
            RunBot.LOG.sendMessage(new MessageBuilder().appendCodeBlock("[" + getCurrentTime() + "]" +
                                                                                "[" + event.getUser().getUsername() +
                                                                                "](ID:" + event.getUser().getId() +
                                                                                ") Is no longer AFK", "md")
                                                       .build());
        } else {

        }
    }

    @Override
    public void onAudioConnect(AudioConnectEvent event) {
        super.onAudioConnect(event);
    }

    @Override
    public void onAudioDisconnect(AudioDisconnectEvent event) {
        super.onAudioDisconnect(event);
    }

    @Override
    public void onAudioUnableToConnect(AudioUnableToConnectEvent event) {
        super.onAudioUnableToConnect(event);
    }

    @Override
    public void onAudioTimeout(AudioTimeoutEvent event) {
        super.onAudioTimeout(event);
    }

    @Override
    public void onAudioRegionChange(AudioRegionChangeEvent event) {
        super.onAudioRegionChange(event);
    }

    public String getCurrentTime() {
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        Date resultdate = new Date(yourmilliseconds);
        return sdf.format(resultdate);
    }
}
