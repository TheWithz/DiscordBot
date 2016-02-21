package events;

import bots.RunBot;
import misc.DiscordConsoleStream;
import misc.Permissions;
import net.dv8tion.jda.events.InviteReceivedEvent;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.InviteUtil;

/**
 * Created by TheWithz on 2/14/16.
 */
public class LoginHandler extends ListenerAdapter {

    @Override
    public void onInviteReceived(InviteReceivedEvent event) {
        //System.out.println(("the invite is null ") + (event.getInvite() == null));
        InviteUtil.join(event.getInvite(), event.getJDA(), x -> System.out.println("Guild " + x.getName() + " has been joined. The invite was sent by " + event.getAuthor()));
        InviteUtil.delete(event.getInvite(), event.getJDA());
    }

    @Override
    public void onReady(ReadyEvent event) {
        // RunBot.API.setDebug(true);
        DiscordConsoleStream console = new DiscordConsoleStream(RunBot.API.getTextChannelById("147169039049949184"));
        console.enableRedirect(true);
        //  BashConsoleStream bash = new BashConsoleStream(RunBot.API.getTextChannelById("148884523487199233"));
        //  bash.enableRedirect(true);
        Permissions.setupPermissions();
        RunBot.BOT = RunBot.API.getUserById(RunBot.API.getSelfInfo().getId());
    }
}
