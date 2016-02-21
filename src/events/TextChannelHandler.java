package events;

import net.dv8tion.jda.events.channel.text.*;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * Created by TheWithz on 2/14/16.
 */
public class TextChannelHandler extends ListenerAdapter {

    public TextChannelHandler() {
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        // if (!event.getAuthor().getUsername().equals("Bot"))
        //    System.out.println(event.getChannel().getId() + " " + event.getChannel().getName());
    }

    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        System.out.println("A TextChannel named: " + event.getChannel().getName() + " was created in guild: " + event.getGuild().getName());
    }

    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        System.out.println("A TextChannel named: " + event.getChannel().getName() + " was deleted from guild: " + event.getGuild().getName());
    }

    @Override
    public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
        System.out.println("TextChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName() + " in guild " + event.getGuild().getName());
    }

    @Override
    public void onTextChannelUpdateTopic(TextChannelUpdateTopicEvent event) {
        System.out.println("The " + event.getChannel().getName() + " TextChannel's topic just from\n" + event.getOldTopic() + "\n to\n" + event.getChannel().getTopic());
    }

    @Override
    public void onTextChannelUpdatePosition(TextChannelUpdatePositionEvent event) {
        System.out.println("The position of " + event.getChannel().getName() + " TextChannl just moved from " + event.getOldPosition() + " to " + event.getChannel().getPosition());
        System.out.println("Be sure to update your channel lists!");
    }

    @Override
    public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event) {
        System.out.println("TextChannel " + event.getChannel().getName() + " " + event.getChannel().getId());
    }

}
