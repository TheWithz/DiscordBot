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
import java.util.Collections;
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
            System.out.println(LinuxCommand.runLinuxCommand("rm latex.tex && rm latex.dvi && rm latex.png && rm latex.aux"));
            System.out.println(LinuxCommand.runLinuxCommand("touch latex.tex"));
            StringBuilder builder = new StringBuilder();
            try {
                JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/commonLatex.json"))));
                obj.keySet().stream().forEach(key -> {
                    if (args[1].equals(key)) {
                        builder.append("\\documentclass{article}\n")
                               .append("\\usepackage{amsmath}")
                               .append("\\pagestyle{empty}\n\n")
                               .append("\\begin{document}\n")
                               .append("\\Huge\n")
                               .append(obj.getString(key))
                               .append("\n")
                               .append("\\end{document}");
                        writeToFile(builder);
                        System.out.println(LinuxCommand.runLinuxCommand("latex latex.tex"));
                        System.out.println(LinuxCommand.runLinuxCommand("dvipng -T tight -x 1200 -z 9 latex.dvi -o latex.png"));
                        event.getChannel().sendFile(new File("latex.png"), new MessageBuilder().appendCodeBlock(args[1], "tex").build());
                        return;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.append("\\documentclass{article}\n")
                   .append("\\usepackage{amsmath}")
                   .append("\\pagestyle{empty}\n\n")
                   .append("\\begin{document}\n")
                   .append("\\Huge\n")
                   .append(args[1])
                   .append("\n")
                   .append("\\end{document}");
            writeToFile(builder);
            System.out.println(LinuxCommand.runLinuxCommand("latex latex.tex"));
            // did you remember to install dvipng? make sure you do
            System.out.println(LinuxCommand.runLinuxCommand("dvipng -T tight -x 1200 -z 9 latex.dvi -o latex.png"));
            event.getChannel().sendFile(new File("latex.png"), new MessageBuilder().appendCodeBlock(args[1], "tex").build());
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
        return Collections.singletonList(RunBot.prefix + "latex");
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
