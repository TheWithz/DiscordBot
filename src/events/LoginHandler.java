package events;

import bots.RunBot;
import events.commands.generator.streams.DiscordConsoleStream;
import misc.Permissions;
import net.dv8tion.jda.events.InviteReceivedEvent;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.sql.SQLException;

/**
 * Created by TheWithz on 2/14/16.
 */
public class LoginHandler extends ListenerAdapter {

    @Override
    public void onInviteReceived(InviteReceivedEvent event) {
        if (event.isPrivate())
            event.getAuthor().getPrivateChannel().sendMessage("I am sorry, but sending invites to bots has been deprecated. However if You really want me to join your server follow this URL \nhttps://discordapp.com/oauth2/authorize?&client_id=168796197396545537&scope=bot&permissions=0");
        //InviteUtil.delete(event.getInvite(), event.getJDA());
    }

    @Override
    public void onReady(ReadyEvent event) {
        // RunBot.API.setDebug(true);
        DiscordConsoleStream console = new DiscordConsoleStream(RunBot.API.getTextChannelById("148884523487199233"), true);
        //  BashConsoleStream bash = new BashConsoleStream(RunBot.API.getTextChannelById("147169039049949184"));
        //  bash.enableRedirect(true);
        Permissions.setupPermissions();
        RunBot.BOT = RunBot.API.getUserById(RunBot.API.getSelfInfo().getId());
        RunBot.API.getAccountManager().setGame("JDA");
        try {
            Permissions.getPermissions().addOp("122764399961309184");
            Permissions.getPermissions().addOp(RunBot.BOT.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
