package events.commands.generator;

import bots.RunBot;
import events.commands.Command;
import events.commands.LinuxCommand;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 4/28/16.
 */
public class LatexCommand extends Command {

    /*\documentclass{article}
    \pagestyle{empty}

    \begin{document}
    $a^2+b^2=c^2$
    \end{document}*/

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (args.length == 2) {
            LinuxCommand.runLinuxCommand(event, "rm latex.tex && rm latex.dvi && rm latex.png && rm latex.aux");
            LinuxCommand.runLinuxCommand(event, "touch latex.tex");
            StringBuilder builder = new StringBuilder();
            try {
                JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/commonLatex.json"))));
                obj.keySet().stream().forEach(key -> {
                    if (args[1].equals(key)) {
                        builder.append("\\documentclass{article}\n");
                        builder.append("\\pagestyle{empty}\n\n");
                        builder.append("\\begin{document}\n");
                        builder.append("\\Huge\n");
                        builder.append("$");
                        builder.append(obj.getString(key));
                        builder.append("$\n");
                        builder.append("\\end{document}");
                        writeToFile(builder);
                        LinuxCommand.runLinuxCommand(event, "latex latex.tex");
                        LinuxCommand.runLinuxCommand(event, "dvipng -T tight -x 1200 -z 9 latex.dvi -o latex.png");
                        event.getChannel().sendFile(new File("latex.png"), new MessageBuilder().appendCodeBlock(args[1], "LaTeX").build());
                        return;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.append("\\documentclass{article}\n");
            builder.append("\\pagestyle{empty}\n\n");
            builder.append("\\begin{document}\n");
            builder.append("$");
            builder.append(args[1]);
            builder.append("$\n");
            builder.append("\\end{document}");
            writeToFile(builder);
            LinuxCommand.runLinuxCommand(event, "latex latex.tex");
            LinuxCommand.runLinuxCommand(event, "dvipng -T tight -x 1200 -z 9 latex.dvi -o latex.png");
            event.getChannel().sendFile(new File("latex.png"), new MessageBuilder().appendCodeBlock(args[1], "LaTeX").build());
        } else if (args.length == 3 && args[1].equals("list")) {
            if (args[2].equals("all")) {
                StringBuilder builder = new StringBuilder();
                try {
                    JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/commonLatex.json"))));
                    obj.keySet().stream().forEach(cLatex -> {
                        builder.append(cLatex);
                        builder.append(" ");
                        builder.append(obj.getString(cLatex));
                        builder.append("\n");
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                event.getChannel().sendMessage(builder.toString());
            } else {
                StringBuilder builder = new StringBuilder();
                try {
                    JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/commonLatex.json"))));
                    builder.append(obj.getString(args[2]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                event.getChannel().sendMessage(builder.toString());
            }
        }

    }

    private void writeToFile(StringBuilder builder) {
        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter("latex.tex", true)))) {

            out.print(builder.toString());

        } catch (IOException error) {
            System.out.println("File not written to tex file");
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "latex");
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
    public List<String> getUsageInstructions() {
        return null;
    }
}
