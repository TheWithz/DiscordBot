package events.commands;

import bots.RunBot;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

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
        RunBot.checkArgs(args, 1, ":x: No language was specified to evaluate. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);

        if (RunBot.OwnerRequired(e))
            return;

        switch (args[1]) {
            case "java":
            case "groovy":
            case "javascript":
                handleJava(e, new DiscordAsOutputStream(e.getTextChannel()), args);
                break;
            case "python":
                handlePython(e, new DiscordAsOutputStream(e.getTextChannel()), args);
                break;
            case "thue":
                handleThue(e, new DiscordAsOutputStream(e.getTextChannel()), args);
                break;
            default:
                sendMessage(e, ":x: Unknown Language argument: `" + args[2] + "` was provided. " +
                        "Please use `" + RunBot.PREFIX + "help " + getAliases().get(0) + "` for more information.");
                break;
        }
    }

    private Object handleJava(MessageReceivedEvent e, DiscordAsOutputStream outStream, String[] args) {
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
            value = shell.evaluate(args[1]);
            System.out.println(":white_check_mark: **Compiled without errors!** \n" + ((value == null) ? "The above code did not return anything." : value));
        } catch (RuntimeException exception) {
            System.out.println(":no_entry: **Did not compile!**");
            System.out.println("```java\n" + exception.getMessage() + "```");
        } finally {
            System.setOut(oldOut);
            outStream.myPrint();
        }
        return value;
    }

    private void handlePython(MessageReceivedEvent e, DiscordAsOutputStream outStream, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No code was specified to evaluate. See " + RunBot.PREFIX + "help " + getAliases().get(0), e);

    }

    private void handleThue(MessageReceivedEvent e, DiscordAsOutputStream outStream, String[] args) {
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
                                                              StringUtils.join(args, " ", 3, args.length),
                                                              args[4]));
            System.out.println(":white_check_mark: **Compiled without errors!** \n" + ((value == null) ? "The above code did not return anything." : value));
        } catch (RuntimeException exception) {
            System.out.println(":no_entry: **Did not compile!**");
            System.out.println("```java\n" + exception.getMessage() + "```");
        } finally {
            System.setOut(oldOut);
            outStream.myPrint();
        }

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
                "(%1$s) <Groovy code>\n" +
                        "[Example:](%1$s) <return \"\\\"5 + 5 is: \\\" + (5 + 5);\">\n" +
                        "<This will print: \"5 + 5 is: 10\">", getAliases().get(0)));
    }
}