package misc;

/**
 * Created by thewithz on 8/28/16.
 */

import bots.RunBot;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Statistics {
    private static int messagesReceived;
    private static int commandsRun;
    private static final Timer TIMER = new Timer();
    //Database Methods
    public static final String GET_MESSAGES_RECEIVED = "getMessages";
    public static final String GET_COMMANDS_RUN = "getCommands";
    public static final String EDIT_MESSAGES_RECEIVED = "editMessages";
    public static final String EDIT_COMMANDS_RUN = "editCommands";
    public static final String EDIT_GUILDS_JOINED = "editGuilds";
    public static final String EDIT_UPTIME = "editUptime";

    static {
        messagesReceived = 0;
        commandsRun = 0;
        try {
            ResultSet sqlMessages = Database.getInstance().getStatement(GET_MESSAGES_RECEIVED).executeQuery();
            while (sqlMessages.next()) {
                messagesReceived = sqlMessages.getInt(1);
            }
            ResultSet sqlCommands = Database.getInstance().getStatement(GET_COMMANDS_RUN).executeQuery();
            while (sqlCommands.next()) {
                commandsRun = sqlCommands.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TIMER.schedule(new TimerTask() {
            public void run() {
                try {
                    PreparedStatement editUptime = Database.getInstance().getStatement(EDIT_UPTIME);
                    editUptime.setString(1, getUptime());
                    if (editUptime.executeUpdate() == 0)
                        throw new SQLException(EDIT_UPTIME + " reported no modified rows!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 250);
    }

    public static void ranCommand(String guildId, String command) throws SQLException {
        commandsRun++;
        PreparedStatement editCommandsRun = Database.getInstance().getStatement(EDIT_COMMANDS_RUN);
        editCommandsRun.setInt(1, commandsRun);
        if (editCommandsRun.executeUpdate() == 0)
            throw new SQLException(EDIT_COMMANDS_RUN + " reported no modified rows!");
    }

    public static void sentMessage(String guildId) throws SQLException {
        messagesReceived++;
        PreparedStatement editMessagesRecieved = Database.getInstance().getStatement(EDIT_MESSAGES_RECEIVED);
        editMessagesRecieved.setInt(1, messagesReceived);
        if (editMessagesRecieved.executeUpdate() == 0)
            throw new SQLException(EDIT_MESSAGES_RECEIVED + " reported no modified rows!");
    }

    public static void joinedLeftGuild() throws SQLException {
        PreparedStatement editGuildsJoined = Database.getInstance().getStatement(EDIT_GUILDS_JOINED);
        editGuildsJoined.setInt(1, RunBot.API.getGuilds().size());
        if (editGuildsJoined.executeUpdate() == 0)
            throw new SQLException(EDIT_GUILDS_JOINED + " reported no modified rows!");
    }

//    public static long messagesPerHour() {
//        return (messagesReceived / (TimeUnit.MILLISECONDS.toHours(getUptime())));
//    }
//
//    public static long commandsPerHour() {
//        return (commandsRun / (TimeUnit.MILLISECONDS.toHours(getUptime())));
//    }

    public static String getUptime() {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long uptime = rb.getUptime();

        long days = TimeUnit.MILLISECONDS.toDays(uptime);
        uptime -= TimeUnit.DAYS.toMillis(days);

        long hours = TimeUnit.MILLISECONDS.toHours(uptime);
        uptime -= TimeUnit.HOURS.toMillis(hours);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime);
        uptime -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime);

        return "**Uptime:** " + days + (days != 1L ? " days " : " day ") +
                hours + (hours != 1L ? " hours " : " hour ") +
                minutes + (minutes != 1L ? " minutes " : " minute ") +
                seconds + (seconds != 1L ? " seconds " : " second");
    }
}
