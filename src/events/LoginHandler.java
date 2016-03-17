package events;

import bots.RunBot;
import events.commands.generator.streams.DiscordConsoleStream;
import misc.Permissions;
import net.dv8tion.jda.events.InviteReceivedEvent;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.InviteUtil;

import java.sql.SQLException;

/**
 * Created by TheWithz on 2/14/16.
 */
public class LoginHandler extends ListenerAdapter {

    @Override
    public void onInviteReceived(InviteReceivedEvent event) {
        if (event.isPrivate())
            InviteUtil.join(event.getInvite(), event.getJDA(), x -> System.out.println("Guild " + x.getName() + " has been joined. The invite was sent by " + event.getAuthor()));
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
