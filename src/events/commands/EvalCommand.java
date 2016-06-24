package events.commands;

import bots.RunBot;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

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
        RunBot.checkArgs(args, 1, ":x: No code was specified to evaluate. See " + RunBot.PREFIX + "help " + getAliases().get(0));

        if (RunBot.OwnerRequired(e))
            return;

        executeScript(e, new DiscordAsOutputStream(e.getTextChannel()), args);
    }

    public Object executeScript(MessageReceivedEvent e, DiscordAsOutputStream outStream, String[] args) {
        Binding binding = new Binding();
        binding.setVariable("event", e);
        binding.setVariable("channel", e.getChannel());
        binding.setVariable("args", args);
        binding.setVariable("api", e.getJDA());
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

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "eval");
    }

    @Override
    public String getDescription() {
        return "Takes Java or Javascript and executes it.";
    }

    @Override
    public String getName() {
        return "Evaluate";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(
                RunBot.PREFIX + "eval <Groovy code>\n" +
                        "    Example: `" + RunBot.PREFIX + "eval return \"5 + 5 is: \" + (5 + 5);\n" +
                        "    This will print: 5 + 5 is: 10");
    }
}