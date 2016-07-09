package events;

import bots.RunBot;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.hooks.ListenerAdapter;
import org.kohsuke.github.*;

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
                    checkCommit();
                    checkPullRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10 * 1000);
    }

    private static void checkPullRequest() {

    }

    private static void checkCommit() throws IOException {
        if (!discordRepo.getPushedAt().equals(lastCommit)) {
            GHCommit commit = discordRepo.listCommits().asList().get(0);
            GHBranch branch = null;
            for (String br : discordRepo.getBranches().keySet()) {
                if (discordRepo.getBranches().get(br).getSHA1().equals(commit.getSHA1())) {
                    branch = discordRepo.getBranches().get(br);
                }
            }
            TextChannel textChannel = RunBot.API.getTextChannelById("147169039049949184");
            textChannel.sendMessageAsync(String.format("***%1$s*** / **%2$s** (%3$s)\n`%4$s` %5$s [%6$s]",
                                                       commit.getAuthor().getLogin(),
                                                       discordRepo.getName(),
                                                       branch,
                                                       commit.getSHA1(),
                                                       commit.getCommitShortInfo().getMessage(),
                                                       github.getMyself().getName()), null);
            lastCommit = discordRepo.getPushedAt();
        }
    }

}
