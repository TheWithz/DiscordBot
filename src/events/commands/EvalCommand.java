package events.commands;

import bots.RunBot;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by TheWithz on 3/4/16.
 */

public class EvalCommand extends Command {

    public EvalCommand() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util);");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {

        if (RunBot.OwnerRequired(e))
            return;

        RunBot.checkArgs(args, 1, ":x: No language was specified to evaluate. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);

        if (e.getAuthor().isBot()) {
            e.getChannel().sendMessage(":x: Bots cannot use this command, for obvious reasons.").queue();
            return;
        }

        switch (args[1]) {
            case "java":
            case "groovy":
            case "javascript":
                handleJava(e, new DiscordAsOutputStream(e.getTextChannel()), args);
                break;
            case "python":
                handlePython(e, args);
                break;
            case "thue":
                handleThue(e, new DiscordAsOutputStream(e.getTextChannel()), args);
                break;
            default:
                e.getChannel().sendMessage(":x: Unknown Language argument: `" +
                                                   args[2] +
                                                   "` was provided. " +
                                                   "Please use `" +
                                                   RunBot.PREFIX +
                                                   "help " +
                                                   getAliases().get(0) +
                                                   "` for more information.").queue();
                break;
        }
    }

    private void handleJava(MessageReceivedEvent e, DiscordAsOutputStream outStream, String[] args) {
        Thread k = new Thread(() -> {
            RunBot.checkArgs(args, 2, ":x: No code was specified to evaluate. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);

            Binding binding = new Binding();
            binding.setVariable("event", e);
            binding.setVariable("channel", e.getChannel());
            binding.setVariable("args", args);
            binding.setVariable("jda", e.getJDA());
            binding.setVariable("bot", RunBot.BOT);
            GroovyShell shell = new GroovyShell(binding);

            // redirect output:
            PrintStream oldOut = System.out;
            Object value = null;
            try {
                System.setOut(new PrintStream(outStream));
                value = shell.evaluate(new StringBuilder().append("import java.util.*;\n")
                                                          .append("import java.math.*;\n")
                                                          .append("import java.net.*;\n")
                                                          .append("import java.io.*;\n")
                                                          .append("import java.util.concurrent.*;\n")
                                                          .append("import java.time.*;\n")
                                                          .append("import java.lang.*;\n")
                                                          .append(args[2])
                                                          .toString());
                System.out.println(":white_check_mark: **Compiled without errors!** \n" + ((value == null) ? "The above code did not return anything." : value));
            } catch (RuntimeException exception) {
                System.out.println(":no_entry: **Did not compile!**");
                System.out.println("```java\n" + exception.getMessage() + "```");
            } finally {
                System.setOut(oldOut);
                outStream.myPrint();
            }
        });
        k.start();
    }

    private void handlePython(MessageReceivedEvent e, String[] args) {
        Thread k = new Thread(() -> {
            RunBot.checkArgs(args, 2, ":x: No code was specified to evaluate. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);

            File f = new File("Template.py");

            try {
                // Create Python file
                f.createNewFile();
                f.deleteOnExit();
                OutputStream stream = new BufferedOutputStream(new FileOutputStream(f));
                stream.write(StringUtils.join(args, " ", 2, args.length).getBytes());
                stream.close();

                // Start process
                ProcessBuilder builder = new ProcessBuilder();
                builder.command("python", f.getName());
                Process p = builder.start();

                // Create Stream Scanner
                Scanner sc = new Scanner(p.getInputStream());
                Scanner scErr = new Scanner(p.getErrorStream());
            /*ChannelListener listener = new ChannelListener(event.channel, 1, m -> { // input
                try
				{
					p.getOutputStream().write(m.getContent().getBytes());
				} catch (IOException e)
				{
					LOG.log(e);
				}
			});*/

                // Read streams
                Thread t = new Thread(() -> {
                    if (sc.hasNext() || scErr.hasNext()) {
                        if (sc.hasNext())
                            e.getChannel().sendMessage(read(sc)).queue();
                        if (scErr.hasNext())
                            e.getChannel().sendMessage(":no_entry: " + read(scErr)).queue();
                    } else
                        e.getChannel().sendMessage(":white_check_mark:").queue();
                }, "PythonEval-Read");
                t.start();

                // Destroy Process
                if (p.waitFor(1, TimeUnit.MINUTES))
                    p.destroy();
                else {
                    p.destroyForcibly();
                    e.getChannel().sendMessage(":x: Process has been terminated. Exceeded time limit.").queue();
                }
                //listener.shutdown();
                //e.getChannel().sendMessageAsync("Process Destroyed", null);
            } catch (Exception ex) {
                e.getChannel().sendMessage(":x: Something went wrong trying to eval your query.").queue();
                e.getChannel().sendMessage("```python\n" + ex.getMessage() + "```").queue();
            }
        });
        k.start();
    }

    private static String read(Scanner in) {
        assert in != null;
        String s = "";

        try {
            while (in.hasNext() && s.length() < 1000) {
                s += in.nextLine() + "\n";
            }
        } catch (IllegalStateException | NoSuchElementException ignored) {
        }
        return s;
    }

    private void handleThue(MessageReceivedEvent e, DiscordAsOutputStream outStream, String[] args) {
        Thread k = new Thread(() -> {
            RunBot.checkArgs(args, 2, ":x: No rules were specified. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);
            RunBot.checkArgs(args, 3, ":x: No content was specified to evaluate. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);
            RunBot.checkArgs(args, 4, ":x: Show_steps was not specified as true or false. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);

            // redirect output:
            PrintStream oldOut = System.out;
            Object value;
            try {
                System.setOut(new PrintStream(outStream));
                value = BashCommand.runLinuxCommand(String.format("python ThueInterpreter.py %1$s:::%2$s %3$s",
                                                                  args[2].replace(" ", ""),
                                                                  args[3],
                                                                  args[4]));
                System.out.println(":white_check_mark: **Compiled without errors!** \n" + ((value == null) ? "The above code did not return anything." : value));
            } catch (RuntimeException exception) {
                System.out.println(":no_entry: **Did not compile!**");
                System.out.println("```java\n" + exception.getMessage() + "```");
            } finally {
                System.setOut(oldOut);
                outStream.myPrint();
            }
        });
        k.start();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "eval");
    }

    @Override
    public String getDescription() {
        return "Takes Java, Javascript, or Groovy code and executes it.";
    }

    @Override
    public String getName() {
        return "Evaluate Command";
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
        return Collections.singletonList(String.format(
                "(%1$s) <language> <code> or [Usage:](%1$s) <thue> <rules> <input> <show trace>\n" +
                        "[Example: 1](%1$s) groovy <return \"\\\"5 + 5 is: \\\" + (5 + 5);\">\n" +
                        "<This will print: \"5 + 5 is: 10\">\n" +
                        "[Example: 2](%1$s) thue <\"a=b; b=c; c=d;\"> <\"aaaaabbbbbbbccccccdddddd\"> <false>\n" +
                        "<This will print: dddddddddddddddddddddddd>", getAliases().get(0)));
    }
}