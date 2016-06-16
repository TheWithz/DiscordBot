package events.commands;

import bots.RunBot;
import net.dv8tion.jda.entities.TextChannel;

import java.io.OutputStream;

/**
 * Created by thewithz on 6/16/16.
 */
public class DiscordAsOutputStream extends OutputStream {

    private StringBuilder anotatedText;
    private StringBuilder finalOutput;
    private TextChannel outChannel;

    public DiscordAsOutputStream(TextChannel outChannel) {
        this.outChannel = outChannel;
        anotatedText = new StringBuilder();
        finalOutput = new StringBuilder();
    }

    @Override
    public void write(int b) {
        if (b == '\n') {
            finalOutput.append(anotatedText.toString());
            anotatedText = new StringBuilder();
            return;
        }
        anotatedText.append((char) b);
    }

    public void myPrint() {
        if (finalOutput.length() <= 2000)
            outChannel.sendMessage(finalOutput.toString());
        else
            RunBot.printAsFile(outChannel, finalOutput, "EvalOutput");
    }
}
