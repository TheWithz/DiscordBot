package bots;

import events.*;
import events.commands.ClearChatCommand;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import javax.security.auth.login.LoginException;

public class RunBot {
    public static JDA API = null;

    public RunBot() {
        try {
            API = new JDABuilder().setEmail("bot@botbot.com").setPassword("Bot").addListener(new TextChannelHandler()).addListener(new VoiceChannelHandler()).addListener(new LoginHandler()).addListener(new AudioHandler()).addListener(new TranslateHandler()).addListener(new CalculatorHandler()).addListener(new ClearChatCommand()).buildAsync();
        } catch (LoginException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Bot failed to connect");
        }
    }

    public static void main(String[] args) {
        RunBot bot = new RunBot();
    }

}
