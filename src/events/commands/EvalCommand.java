package events.commands;

import bots.RunBot;
import groovy.lang.Binding;
import groovy.lang.GString;
import groovy.lang.GroovyShell;
import jdk.internal.org.xml.sax.ErrorHandler;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.org.xml.sax.SAXParseException;
import misc.Permissions;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.http.protocol.ExecutionContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by TheWithz on 3/4/16.
 */

public class EvalCommand extends Command {
    private ScriptEngine engine;

    public EvalCommand() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
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
        executeScript(e, System.out, args);
    }

    public Object executeScript(MessageReceivedEvent e, PrintStream outStream,
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
        Object value;
        try {
            System.setOut(outStream);
            value = shell.evaluate(e.getMessage().getContent().substring(args[0].length()));
            e.getChannel().sendMessage("**Compiled without errors!** \n" + ((value == null) ? value : value));
        } finally {
            System.setOut(oldOut);
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
                        "    Example: `$eval return \"5 + 5 is: \" + (5 + 5);\n" +
                        "    This will print: 5 + 5 is: 10");
    }
}