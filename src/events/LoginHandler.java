package events;

import bots.RunBot;
import misc.BashConsoleStream;
import misc.DiscordConsoleStream;
import net.dv8tion.jda.events.InviteReceivedEvent;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.InviteUtil;

/**
 * Created by TheWithz on 2/14/16.
 */
public class LoginHandler extends ListenerAdapter {

    public LoginHandler() {

    }

    @Override
    public void onInviteReceived(InviteReceivedEvent event) {
        InviteUtil.join(event.getInvite(), event.getJDA(), x -> System.out.println("Guild " + x.getName() + " has been joined."));
        InviteUtil.delete(event.getInvite(), event.getJDA());
    }

    @Override
    public void onReady(ReadyEvent event) {
        DiscordConsoleStream console = new DiscordConsoleStream(RunBot.API.getTextChannelById("147169039049949184"));
        console.enableRedirect(true);
        BashConsoleStream bash = new BashConsoleStream(RunBot.API.getTextChannelById("148884523487199233"));
        bash.enableRedirect(true);
    }
}
