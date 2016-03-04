package events.commands;

import bots.RunBot;
import misc.Permissions;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Collections;
import java.util.List;

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

        try {
            engine.put("event", e);
            engine.put("channel", e.getChannel());
            engine.put("args", args);
            engine.put("api", e.getJDA());
            engine.put("bot", RunBot.BOT);
            Object out = engine.eval(
                    "(function() {" +
                            "with (imports) {" +
                            e.getMessage().getContent().substring(args[0].length()) +
                            "}" +
                            "})();");
            e.getChannel().sendMessage(out == null ? "Executed without error. \n": out.toString());
        } catch (ScriptException e1) {
            e.getChannel().sendMessage(e1.getMessage());
        }
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("$eval");
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
                "$eval <Java code>\n" +
                        "    Example: `$eval return \"5 + 5 is: \" + (5 + 5);\n" +
                        "    This will print: 5 + 5 is: 10");
    }
}