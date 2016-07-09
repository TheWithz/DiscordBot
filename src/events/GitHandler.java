package events;

import bots.RunBot;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.hooks.ListenerAdapter;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by thewithz on 7/6/16.
 */
public class GitHandler extends ListenerAdapter {
    private static GitHub github;
    private static Timer timer = new Timer();
    private static Date lastCommit;
    private static GHRepository discordRepo;

    public GitHandler(String gitApiToken, String gitUserName) {
        try {
            github = new GitHubBuilder().withOAuthToken(gitApiToken, gitUserName).build();
            discordRepo = github.getMyself().getRepository("DiscordBot");
            lastCommit = discordRepo.getPushedAt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    DV8FromTheWorld/JDA (development)
aa9fd8b Fixed issues when JDA received a create event for a new Private channel. [Austin Keener]
     */

    static void startTimer() {
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    discordRepo = github.getMyself().getRepository("DiscordBot");
                    if (!discordRepo.getPushedAt().equals(lastCommit)) {

                        TextChannel textChannel = RunBot.API.getTextChannelById("147169039049949184");
                        textChannel.sendMessageAsync(String.format("***%1$s*** / **%2$s** (%3$s)\n`%4$s` this is where the commit message will go when I figure out how to do it " +
                                                                           "[%5$s]", discordRepo.getCollaborators().byLogin("TheWithz").toString(),
                                                                   discordRepo.getName(),
                                                                   discordRepo.getDefaultBranch(),
                                                                   discordRepo.getId(), github.getMyself().getName()), null);
                        lastCommit = discordRepo.getPushedAt();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10 * 1000);
    }

}
