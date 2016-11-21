package events;

import bots.RunBot;
import misc.Permissions;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.SQLException;

import static bots.RunBot.API;

/**
 * Created by TheWithz on 2/14/16.
 */
public class LoginHandler extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        Permissions.setupPermissions();
        RunBot.BOT = API.getUserById(API.getSelfUser().getId());
        API.getPresence().setGame(Game.of("JDA"));
        //LeagueHandler.startTimer();
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
