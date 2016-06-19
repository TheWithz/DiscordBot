package events.commands;

import bots.RunBot;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import misc.Permissions;
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
    private StringBuilder builder = new StringBuilder();

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
        if (!Permissions.getPermissions().isOp(e.getAuthor())) {
            e.getChannel().sendMessage("Sorry, this command is OP only!");
            return;
        }
        executeScript(e, new DiscordAsOutputStream(e.getTextChannel()), new DiscordAsErrorStream(e.getTextChannel()), args);
    }

    public Object executeScript(MessageReceivedEvent e, DiscordAsOutputStream outStream, DiscordAsErrorStream errStream,
                                String[] args) {
        Binding binding = new Binding();
        binding.setVariable("event", e);
        binding.setVariable("channel", e.getChannel());
        binding.setVariable("args", args);
        binding.setVariable("api", e.getJDA());
        binding.setVariable("bot", RunBot.BOT);
        GroovyShell shell = new GroovyShell(binding);

        // redirect output:
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;
        Object value;
        try {
            System.setOut(new PrintStream(outStream));
            System.setErr(new PrintStream(errStream));
            value = shell.evaluate(args[1]);
            System.out.println(":white_check_mark: **Compiled without errors!** \n" + ((value == null) ? "The above code did not return anything." : value));
        } finally {
            System.setOut(oldOut);
            System.setErr(oldErr);
            outStream.myPrint();
            errStream.myPrint();
        }
        return value;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.prefix + "eval");
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
                RunBot.prefix + "eval <Java code>\n" +
                        "    Example: `" + RunBot.prefix + "eval return \"5 + 5 is: \" + (5 + 5);\n" +
                        "    This will print: 5 + 5 is: 10");
    }
}