package bots;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BotListener extends ListenerAdapter {
    private final JDA API;
    private ArrayList<String> badWords;
    private boolean profanityBot;
    private static final Timer TIMER = new Timer();

    public BotListener(JDA API) {
        this.API = API;
        TIMER.schedule(new TimerTask() {
            public void run() {

            }
        }, 0, 300 * 1000); // runs every 5 minutes
    }

}
