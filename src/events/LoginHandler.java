package events;

import bots.RunBot;
import misc.Permissions;
import net.dv8tion.jda.events.InviteReceivedEvent;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.sql.SQLException;

import static bots.RunBot.API;

/**
 * Created by TheWithz on 2/14/16.
 */
public class LoginHandler extends ListenerAdapter {

    @Override
    public void onInviteReceived(InviteReceivedEvent event) {
        if (event.isPrivate())
            event.getAuthor()
                 .getPrivateChannel()
                 .sendMessage("I am sorry, but sending invites to bots has been deprecated. However if You really want me to join your server follow this URL \nhttps://discordapp.com/oauth2/authorize?&client_id=168796197396545537&scope=bot&permissions=0");
    }

    @Override
    public void onReady(ReadyEvent event) {
        // RunBot.API.setDebug(true);
        Permissions.setupPermissions();
        RunBot.BOT = API.getUserById(API.getSelfInfo().getId());
        RunBot.LOG = RunBot.API.getTextChannelById("193015102817959936");
        API.getAccountManager().setGame("JDA");
        LeagueHandler.startTimer();
        GitHandler.startTimer();
        RunBot.OWNER_REQUIRED = ":no_entry: Only " + RunBot.API.getUserById("122764399961309184").getAsMention() + " can use this command";
        try {
            // add TheWithz as OP
            Permissions.getPermissions().addOp("122764399961309184");
            Permissions.getPermissions().addOp(RunBot.BOT.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
