package bots;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.audio.player.Player;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.channel.text.*;
import net.dv8tion.jda.events.channel.voice.*;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BotListener extends ListenerAdapter {
    private final JDA API;
    private ArrayList<String> badWords;
    private boolean profanityBot;
    private static final Timer TIMER = new Timer();

    public BotListener(JDA API) {
        this.API = API;
        fillBadWords();
        TIMER.schedule(new TimerTask() {
            public void run() {

            }
        }, 0, 300 * 1000); // runs every 5 minutes
    }

    private void removeBadWords(MessageReceivedEvent e) {
        /*
        if (profanityBot && containsBadWords(e.getMessage().getContent().toLowerCase())) {
            GroupUser user = e.getUser();
            e.getMsg().deleteMessage();
            e.getGroup()
                    .sendMessage(new MessageBuilder()
                            .addString(user.toString() + "'s message has been deleted because it contained profanity.")
                            .build(API));
        }
        */
    }

    private boolean containsBadWords(String message) {
        String[] words = message.split("\\s+");
        for (String word1 : badWords) {
            for (String word2 : words) {
                if (word2.equals(word1))
                    return true;
            }
        }
        return false;
    }

    private void writeLog(MessageReceivedEvent e) {
        /*
        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter("resources/log.txt", true)))) {
            if (e.getMsg().isEdited()) {
                out.println(e.getUser().getUser().getUsername() + " | " + e.getGroup().getName() + " | "
                        + getCurrentTimeStamp());
                out.println("\t" + e.getMsg().getMessage() + " | EDITTED");
            } else {
                out.println(e.getUser().getUser().getUsername() + " | " + e.getGroup().getName() + " | "
                        + getCurrentTimeStamp());
                out.println("\t" + e.getMsg().getMessage());
            }

        } catch (IOException error) {
            System.out.println("File not written to text file");
        }
        */
    }

    private static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    private void handleCommands(MessageReceivedEvent e) {
        /*
        String[] commandArguments = e.getMsg().getMessage().toLowerCase().split("\\s+");
        switch (commandArguments[0]) {
            case "$tgpf":
                toggleProfanity(e);
                break;
            case "$rnum":
                generateRandomNumber(e, commandArguments);
                break;
            case "$role":
                generateLeagueRole(e, commandArguments);
                break;
            case "$help":
                showHelp(e, commandArguments);
                break;
            case "$clch":
                if (e.getUser().getUser().getId().equals(DiscordUser.NATHAN.getID())
                        || e.getUser().getUser().getId().equals(DiscordUser.MAX.getID()))
                    clearChat(e, commandArguments);
                else
                    e.getGroup().sendMessage(new MessageBuilder().addString("Sorry " + e.getUser().getUser().getUsername()
                            + ". You do not have permission to use this command.").build(API));
                break;
            case "$navy":
                writeNavy(e);
                break;
            case "$calc":
                calculate(e, commandArguments);
                break;
            case "$fact":
                generateRandomFact(e);
                break;
            default:
                break;
        }
        */
    }

    private void fillBadWords() {
        badWords = new ArrayList<String>();
        badWords.add("fuck");
        badWords.add("shit");
        badWords.add("cunt");
        badWords.add("dick");
        badWords.add("fag");
        badWords.add("ass");
        badWords.add("bitch");
        badWords.add("nigger");
        badWords.add("gay");
        badWords.add("cock");
        badWords.add("dike");
        badWords.add("douche");
        badWords.add("dildo");
        badWords.add("hoe");
        badWords.add("jiz");
        badWords.add("cum");
        badWords.add("kike");
        badWords.add("negro");
        badWords.add("tard");
        badWords.add("vag");
        badWords.add("penis");
        badWords.add("pussy");
        badWords.add("tit");
        profanityBot = false;
    }
    /*
    private void generateRandomFact(UserChatEvent e) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("resources/randomFacts.txt"));
        } catch (FileNotFoundException m) {
            m.printStackTrace();
        }
        ArrayList<String> a = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            a.add(scanner.nextLine());
        }
        int rnum = (int) (a.size() * Math.random());
        e.getGroup().sendMessage(new MessageBuilder().addString(a.get(rnum)).build(API));
        scanner.close();
    }

    private void showHelp(UserChatEvent e, String[] commandArguments) {
        if (commandArguments[0].substring(0, 1).equals("$")) {
            e.getGroup().sendMessage(new MessageBuilder().addString("Here is a list of commands and their uses.").build(API));
            e.getGroup().sendMessage(
                    new MessageBuilder().addString("1. $help | *lists all commands and their syntax*").build(API));
            e.getGroup().sendMessage(new MessageBuilder()
                    .addString("2. $rnum <Number> | *returns a number between 1 and <Number>*").build(API));
            e.getGroup().sendMessage(new MessageBuilder()
                    .addString("3. $role <Role> | *returns a league champion that is good at <Role>*").build(API));
            e.getGroup().sendMessage(
                    new MessageBuilder().addString("4. $clch <User> | *clears chat of <User>'s messages*").build(API));
            e.getGroup().sendMessage(
                    new MessageBuilder().addString("5. $tgpf | *toggles profanity filter on or off*").build(API));
            e.getGroup()
                    .sendMessage(new MessageBuilder().addString("6. $navy | *returns navy seals' copy pasta*").build(API));
            e.getGroup()
                    .sendMessage(new MessageBuilder()
                            .addString(
                                    "7. $calc <Mathematical Expression> | *returns the answer to the <Mathematical Expression>*")
                            .build(API));
            e.getGroup()
                    .sendMessage(new MessageBuilder()
                            .addString(
                                    "8. $fact | *returns a random fact*")
                            .build(API));
            e.getGroup()
                    .sendMessage(new MessageBuilder()
                            .addString(
                                    "9. $tran <From Language> <To Language> <Message> | *returns <Message> translated from <From Language> to <To Language>*")
                            .build(API));
        }

    }

    private void generateLeagueRole(UserChatEvent e, String[] commandArguments) {
        Scanner Ronald = null;
        try {
            Ronald = new Scanner(new File("resources/ChampionsByRole.txt"));
        } catch (FileNotFoundException m) {
            m.printStackTrace();
        }
        int i = -1;
        ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>();
        a.add(new ArrayList<String>()); // top
        a.add(new ArrayList<String>()); // mid
        a.add(new ArrayList<String>()); // jungle
        a.add(new ArrayList<String>()); // adc
        a.add(new ArrayList<String>()); // support
        while (Ronald.hasNextLine()) {
            String x = Ronald.nextLine();
            if (x.substring(0, 1).equals(":")) {
                ++i;
            }
            a.get(i).add(x);
        }
        Ronald.close();
        try {
            String x = commandArguments[1].toLowerCase();
            switch (x) {
                case "top":
                    String k = a.get(0).get((int) (Math.random() * a.get(0).size() + 1));
                    e.getGroup().sendMessage(new MessageBuilder().addString(k).build(API));
                    break;

                case "mid":
                    k = a.get(1).get((int) (Math.random() * a.get(1).size() + 1));
                    e.getGroup().sendMessage(new MessageBuilder().addString(k).build(API));
                    break;

                case "jun":
                    k = a.get(2).get((int) (Math.random() * a.get(2).size() + 1));
                    e.getGroup().sendMessage(new MessageBuilder().addString(k).build(API));
                    break;

                case "adc":
                    k = a.get(3).get((int) (Math.random() * a.get(3).size() + 1));
                    e.getGroup().sendMessage(new MessageBuilder().addString(k).build(API));
                    break;

                case "sup":
                    k = a.get(4).get((int) (Math.random() * a.get(4).size() + 1));
                    e.getGroup().sendMessage(new MessageBuilder().addString(k).build(API));
                    break;

                default:
                    e.getGroup().sendMessage(new MessageBuilder()
                            .addString("Role not found. make sure you are using top/mid/jungle/adc/support").build(API));
                    break;
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
    }

    private void generateRandomNumber(UserChatEvent e, String[] commandArguments) {
        try {
            long rnum = (long) (Long.parseLong(commandArguments[1]) * Math.random() + 1);
            e.getGroup().sendMessage(new MessageBuilder().addString("Your number is: " + rnum).build(API));
        } catch (NumberFormatException error) {
            e.getGroup().sendMessage(new MessageBuilder().addString("That number is too big.").build(API));
        }

    private void toggleProfanity(UserChatEvent e) {
        if (profanityBot) {
            profanityBot = false;
            e.getGroup().sendMessage(new MessageBuilder().addString("Profanity Bot has been turned off").build(API));
        } else {
            profanityBot = true;
            e.getGroup().sendMessage(new MessageBuilder().addString("Profanity Bot has been turned on").build(API));
        }
    }*/

}
