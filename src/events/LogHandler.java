package events;

import bots.RunBot;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.audio.AudioRegionChangeEvent;
import net.dv8tion.jda.events.audio.AudioTimeoutEvent;
import net.dv8tion.jda.events.audio.AudioUnableToConnectEvent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TheWithz on 2/21/16.
 */
public class LogHandler extends ListenerAdapter {
    public static ArrayList<String> logGrouper = new ArrayList<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

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
        if (event.getChannel().getId().equals("193015102817959936") && event.getAuthor()
                                                                            .getId()
                                                                            .equals(RunBot.BOT.getId())) {
            return;
        }
        User user = event.getAuthor();
        logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Wrote message in channel/guild [%s](%s)```",
                                     getCurrentTime(),
                                     user.getUsername(),
                                     user.getId(),
                                     event.getChannel().getName(),
                                     event.getGuild().getName()));
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
        logGrouper.add(String.format("```md\n[%s][TC:%s][%s](%s) Channel was deleted```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName()));
    }

    @Override
    public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
        super.onTextChannelUpdateName(event);
        logGrouper.add(String.format("```md\n[%s][TC:%s][%s](%s) Channel renamed from/to [%s](%s)```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName(),
                                     event.getOldName(),
                                     event.getChannel().getName()));
    }

    @Override
    public void onTextChannelUpdateTopic(TextChannelUpdateTopicEvent event) {
        super.onTextChannelUpdateTopic(event);
        logGrouper.add(String.format("```md\n[%s][TC:%s][%s](%s) Channel topic was updated```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName()));
    }

    @Override
    public void onTextChannelUpdatePosition(TextChannelUpdatePositionEvent event) {
        super.onTextChannelUpdatePosition(event);
        logGrouper.add(String.format("```md\n[%s][TC:%s][ID:%s](%s) Channel was moved out of position [%s]```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName(),
                                     event.getOldPosition()));
    }

    @Override
    public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event) {
        super.onTextChannelUpdatePermissions(event);
        logGrouper.add(String.format("```md\n[%s][TC:%s][ID:%s](%s) Channel permissions were updated \n Changed Roles: [%s]\n Users with " +
                                             "Permissions: (%s)```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName(),
                                     event.getChangedRoles().toString(),
                                     event.getUsersWithPermissionChanges().toString()));
    }

    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        super.onTextChannelCreate(event);
        logGrouper.add(String.format("```md\n[%s][TC:%s][%s](%s) Channel was created```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName()));
    }

    @Override
    public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {
        super.onVoiceChannelUpdateName(event);
        logGrouper.add(String.format("```md\n[%s][VC:%s][%s](%s) Channel renamed from/to [%s](%s)```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName(),
                                     event.getOldName(),
                                     event.getChannel().getName()));
    }

    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        super.onVoiceChannelDelete(event);
        logGrouper.add(String.format("```md\n[%s][VC:%s][%s](%s) Channel was deleted```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName()));
    }

    @Override
    public void onVoiceChannelUpdatePosition(VoiceChannelUpdatePositionEvent event) {
        super.onVoiceChannelUpdatePosition(event);
        logGrouper.add(String.format("```md\n[%s][VC:%s][ID:%s](%s) Channel was moved out of position [%s]```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName(),
                                     event.getOldPosition()));
    }

    @Override
    public void onVoiceChannelUpdateUserLimit(VoiceChannelUpdateUserLimitEvent event) {
        super.onVoiceChannelUpdateUserLimit(event);
        logGrouper.add(String.format("```md\n[%s][VC:%s][ID:%s](%s) Channel user limit changed from/to [%s](%s)```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName(),
                                     event.getOldUserLimit(),
                                     event.getChannel().getUserLimit()));
    }

    @Override
    public void onVoiceChannelUpdateBitrate(VoiceChannelUpdateBitrateEvent event) {
        super.onVoiceChannelUpdateBitrate(event);
        logGrouper.add(String.format("```md\n[%s][VC:%s][ID:%s](%s) Channel bitrate changed from/to [%s](%s)```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName(),
                                     event.getOldBitrate(),
                                     event.getChannel().getBitrate()));
    }

    @Override
    public void onVoiceChannelUpdatePermissions(VoiceChannelUpdatePermissionsEvent event) {
        super.onVoiceChannelUpdatePermissions(event);
        logGrouper.add(String.format("```md\n[%s][VC:%s][ID:%s](%s) Channel permissions were updated \n Changed Roles: [%s]\n Users with " +
                                             "Permissions: (%s)```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName(),
                                     event.getChangedRoles().toString(),
                                     event.getUsersWithPermissionChanges().toString()));
    }

    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
        super.onVoiceChannelCreate(event);
        logGrouper.add(String.format("```md\n[%s][VC:%s][%s](%s) Channel was created```",
                                     getCurrentTime(),
                                     event.getChannel().getName(),
                                     event.getChannel().getId(),
                                     event.getGuild().getName()));
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
        logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Joined Guild {%s}```",
                                     getCurrentTime(),
                                     event.getUser().getUsername(),
                                     event.getUser().getId(),
                                     event.getGuild().getName()));
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        super.onGuildMemberLeave(event);
        logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Left Guild {%s}```",
                                     getCurrentTime(),
                                     event.getUser().getUsername(),
                                     event.getUser().getId(),
                                     event.getGuild().getName()));
    }

    @Override
    public void onGuildMemberBan(GuildMemberBanEvent event) {
        super.onGuildMemberBan(event);
        logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Was banned from Guild [%s]```",
                                     getCurrentTime(),
                                     event.getUser().getUsername(),
                                     event.getUser().getId(),
                                     event.getGuild().getName()));
    }

    @Override
    public void onGuildMemberUnban(GuildMemberUnbanEvent event) {
        super.onGuildMemberUnban(event);
        logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Was unbanned from Guild [%s]```",
                                     getCurrentTime(),
                                     event.getUser().getUsername(),
                                     event.getUser().getId(),
                                     event.getGuild().getName()));
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        super.onGuildMemberRoleAdd(event);
        logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Had a role added. role/guild [%s](%s)```",
                                     getCurrentTime(),
                                     event.getUser().getUsername(),
                                     event.getUser().getId(),
                                     event.getGuild().getName(),
                                     event.getRoles()));
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        super.onGuildMemberRoleRemove(event);
        logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Had a role removed. role/guild [%s](%s)```",
                                     getCurrentTime(),
                                     event.getUser().getUsername(),
                                     event.getUser().getId(),
                                     event.getGuild().getName(),
                                     event.getRoles()));
    }

    @Override
    public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
        super.onGuildMemberNickChange(event);
        User user = event.getUser();
        String name = user.getUsername();
        String oldNick = event.getPrevNick() == null ? name : event.getPrevNick();
        String newNick = event.getNewNick() == null ? name : event.getNewNick();
        logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Changed their nickname on Guild [%s] from/to [%s](%s)```",
                                     getCurrentTime(),
                                     name,
                                     user.getId(),
                                     event.getGuild().getName(),
                                     oldNick,
                                     newNick));
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
    public void onVoiceServerMute(VoiceServerMuteEvent event) {
        super.onVoiceServerMute(event);
        // TODO 6/17/16 make sure when server mute or deafen are called, regular mute and deafen are not
        if (event.getVoiceStatus().isServerMuted()) {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Was server muted in Guild [%s]```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId(),
                                         event.getGuild().getName()));
        } else {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Was server unmuted in Guild [%s]```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId(),
                                         event.getGuild().getName()));
        }
    }

    @Override
    public void onVoiceServerDeaf(VoiceServerDeafEvent event) {
        super.onVoiceServerDeaf(event);
        // TODO 6/17/16 make sure when server mute or deafen are called, regular mute and deafen are not
        if (event.getVoiceStatus().isServerDeaf()) {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Was server deafened in Guild [%s]```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId(),
                                         event.getGuild().getName()));
        } else {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Was server undeafened in Guild [%s]```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId(),
                                         event.getGuild().getName()));
        }
    }

    @Override
    public void onVoiceMute(VoiceMuteEvent event) {
        super.onVoiceMute(event);
        if (event.getVoiceStatus().isMuted()) {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Muted themselves```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId()));
        } else {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Unmuted themselves```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId()));
        }
    }

    @Override
    public void onVoiceDeaf(VoiceDeafEvent event) {
        super.onVoiceDeaf(event);
        if (event.getVoiceStatus().isDeaf()) {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Deafened themselves```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId()));
        } else {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Undeafened themselves```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId()));
        }
    }

    @Override
    public void onVoiceJoin(VoiceJoinEvent event) {
        super.onVoiceJoin(event);
        // if they entered the AFK channel
        if (RunBot.API.getVoiceChannelByName("AFK").contains(event.getChannel())) {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Has gone AFK```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId()));
        } else {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Joined channel/guild [%s](%s)```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId(),
                                         event.getChannel().getName(),
                                         event.getGuild().getName()));
        }
    }

    @Override
    public void onVoiceLeave(VoiceLeaveEvent event) {
        super.onVoiceLeave(event);
        // if they left the AFK channel
        if (RunBot.API.getVoiceChannelByName("AFK").contains(event.getOldChannel())) {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Is no longer AFK```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId()));
        } else {
            logGrouper.add(String.format("```md\n[[%s][%s]](ID:%s) Left channel/guild [%s](%s)```",
                                         getCurrentTime(),
                                         event.getUser().getUsername(),
                                         event.getUser().getId(),
                                         event.getOldChannel().getName(),
                                         event.getGuild().getName()));
        }
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
