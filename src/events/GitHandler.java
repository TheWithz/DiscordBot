package events;

import bots.RunBot;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.hooks.ListenerAdapter;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static bots.RunBot.CLIENT;

/**
 * Created by thewithz on 7/6/16.
 */
public class GitHandler extends ListenerAdapter {
    private static Timer timer = new Timer();
    private static Date lastCommit;
    private static Repository discordRepo;

    public GitHandler(String gitApiToken, String gitUserName, String gitPassword) {
        CLIENT.setOAuth2Token(gitApiToken).setCredentials(gitUserName, gitPassword);
        RepositoryService service = new RepositoryService();
        try {
            service.getRepositories(CLIENT.getUser()).stream().filter(repo -> repo.getName().equals("DiscordBot")).forEach(repo -> {
                lastCommit = repo.getPushedAt();
                discordRepo = repo;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startTimer() {
        timer.schedule(new TimerTask() {
            public void run() {
                if (discordRepo.getPushedAt() != lastCommit) {
                    TextChannel textChannel = RunBot.API.getTextChannelById("147169039049949184");
                    textChannel.sendMessageAsync(":white_check_mark: a new Commit has been pushed to DiscordBot", null);
                    lastCommit = discordRepo.getPushedAt();
                }
            }
        }, 0, 3 * 1000); // runs every 5 seconds *i think*
    }
}
