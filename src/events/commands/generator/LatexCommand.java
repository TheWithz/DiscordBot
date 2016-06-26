package events.commands.generator;

import bots.RunBot;
import events.commands.BashCommand;
import events.commands.Command;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/28/16.
 */
public class LatexCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 1, ":x: No Action argument was provided. Please use `" + RunBot.PREFIX + "help " + getAliases().get(0) + "` for more information.", event);

        switch (args[1]) {
            case "show":
                handleShow(event, args);
                break;
            case "list":
                handleList(event);
                break;
            default:
                break;
        }
    }

    private void handleList(MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder();
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("commonLatex.json"))));
            builder.append("```fix\nLaTeX presets``````tex\n");
            ArrayList<String> presets = new ArrayList(obj.keySet());
            for (int i = 0; i < presets.size(); i++) {
                builder.append(i).append(") ")
                       .append(presets.get(i))
                       .append(" | ")
                       .append(obj.getString(presets.get(i)))
                       .append("\n");
            }
        } catch (IOException e) {
            sendMessage(event, ":x: " + e.getMessage());
        }
        event.getChannel().sendMessage(builder.append("```").toString());
    }

    private void handleShow(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, "No modifier provided. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        switch (args[2]) {
            case "preset":
                handleShowPresets(event, args);
                break;
            case "raw":
            case "code":
            case "latex":
            case "tex":
                handleRaw(event, args);
                break;
            default:
                break;
        }
    }

    private void handleRaw(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 3, ":x: No latex provided. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        System.out.println(BashCommand.runLinuxCommand("rm latex.tex && rm latex.dvi && rm latex.png && rm latex.aux"));
        System.out.println(BashCommand.runLinuxCommand("touch latex.tex"));

        String[] latex = ArrayUtils.subarray(args, 3, args.length);

        StringBuilder builder = buildLatex(new StringBuilder(), latex);

        writeToFile(builder);
        System.out.println(BashCommand.runLinuxCommand("latex latex.tex"));
        // did you remember to install dvipng? make sure you do
        System.out.println(BashCommand.runLinuxCommand("dvipng -T tight -x 1200 -z 9 latex.dvi -o latex.png"));
        event.getChannel().sendFile(new File("latex.png"), new MessageBuilder().appendCodeBlock(Arrays.toString(latex), "tex").build());
    }

    private void handleShowPresets(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 3, ":x: No preset provided. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        System.out.println(BashCommand.runLinuxCommand("rm latex.tex && rm latex.dvi && rm latex.png && rm latex.aux"));
        System.out.println(BashCommand.runLinuxCommand("touch latex.tex"));

        StringBuilder builder = new StringBuilder();

        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("commonLatex.json"))));
            if (obj.keySet().contains(args[3])) {
                System.out.println(obj.getString(args[3]));
                builder = buildLatex(builder, obj.getString(args[3]));
                writeToFile(builder);
                // error here
                System.out.println(BashCommand.runLinuxCommand("latex latex.tex"));
                System.out.println(BashCommand.runLinuxCommand("dvipng -T tight -x 1200 -z 9 latex.dvi -o latex.png"));
                event.getChannel().sendFile(new File("latex.png"), new MessageBuilder().appendCodeBlock(args[3], "tex").build());
            }
        } catch (IOException e) {
            sendMessage(event, ":x: " + e.getMessage());
        }
    }

    @NotNull
    private StringBuilder buildLatex(StringBuilder builder, String... strs) {
        builder.append("\\documentclass{article}\n")
               .append("\\usepackage{amsmath}")
               .append("\\pagestyle{empty}\n\n")
               .append("\\begin{document}\n")
               .append("\\Huge\n");
        for (int i = 0; i < strs.length; i++) {
            builder.append(strs[i]).append("\n");
        }
        return builder.append("\\end{document}");
    }

    private void writeToFile(StringBuilder builder) {
        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter("latex.tex", true)))) {

            out.print(builder.toString());

        } catch (IOException error) {
            System.out.println(":x: File not written to tex file");
        }
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "latex");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsEveryone() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOp() {
        return null;
    }

    @Override
    public List<String> getUsageInstructionsOwner() {
        return null;
    }
}
