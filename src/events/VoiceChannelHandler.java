package events;

import net.dv8tion.jda.events.channel.voice.*;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * Created by TheWithz on 2/14/16.
 */
public class VoiceChannelHandler extends ListenerAdapter {

    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
        System.out.println("A VoiceChannel named: " + event.getChannel().getName() + " was created in guild: " + event.getGuild().getName());
    }

    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        System.out.println("A VoiceChannel named: " + event.getChannel().getName() + " was deleted from guild: " + event.getGuild().getName());
    }

    @Override
    public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {
        System.out.println("VoiceChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName() + " in guild " + event.getGuild().getName());
    }

    @Override
    public void onVoiceChannelUpdatePosition(VoiceChannelUpdatePositionEvent event) {
        System.out.println("The position of " + event.getChannel().getName() + " VoiceChannel just moved from " + event.getOldPosition() + " to " + event.getChannel().getPosition());
        System.out.println("Be sure to update your channel lists!");
    }

    @Override
    public void onVoiceChannelUpdatePermissions(VoiceChannelUpdatePermissionsEvent event) {
        System.out.println("VoiceChannel " + event.getChannel().getName());
    }

}
