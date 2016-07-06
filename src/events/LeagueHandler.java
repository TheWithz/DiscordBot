package events;

import bots.RunBot;
import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.staticdata.Champion;
import com.robrua.orianna.type.core.summoner.Summoner;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static bots.RunBot.REGION;

/**
 * Created by thewithz on 7/5/16.
 */
public class LeagueHandler extends ListenerAdapter {
    private static Timer timer = new Timer();

    public LeagueHandler(String riotApiToken) {
        RiotAPI.setRegion(REGION);
        RiotAPI.setAPIKey(riotApiToken);
    }

    public static void startTimer() {
        Summoner summoner = RiotAPI.getSummonerByName("YourMomsClit");
        Champion champion = RiotAPI.getChampionByName("Ashe");
        System.out.println(summoner.getName() + " is a level " + summoner.getLevel() + " summoner on the NA server.");
        System.out.println("He enjoys playing LoL on all different champions, like " + Arrays.toString(summoner.getTopChampionMastery(3).toArray()) + ".");
        timer.schedule(new TimerTask() {
            public void run() {
                if (summoner.getCurrentGame() != null) {
                    summoner.getCurrentGame()
                            .getParticipants()
                            .stream()
                            .filter(participant -> participant.getSummonerID() == summoner.getID())
                            .filter(participant -> summoner.getCurrentGame().getLength() < 200)
                            .filter(participant -> participant.getChampionID() == champion.getID())
                            .filter(participant -> summoner.getCurrentGame().getQueueType().toString().contains("RANKED"))
                            .forEach(participant -> RunBot.API.getTextChannelById("147169039049949184")
                                                              .sendMessageAsync(new MessageBuilder().appendString("oh god... Alex is playing Ashe again :cry:").build(), null));
                }
            }
        }, 0, 3 * 1000); // runs every 5 seconds *i think*
    }
}
