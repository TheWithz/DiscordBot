package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import misc.Permissions;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TheWithz on 4/29/16.
 */
public class SaveCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (!Permissions.getPermissions().isOp(event.getAuthor())) {
            event.getChannel().sendMessage("Sorry, this command is OP only");
            return;
        }
        if (args.length == 4 && args[1].equals("latex")) {
            try {
                JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/commonLatex.json"))));
                obj.put(args[2], args[3]);
                Files.write(Paths.get("resources/commonLatex.json"), obj.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 4 && args[1].equals("playlist")) {
            try {
                JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Playlists.json"))));
                obj.put(args[2], args[3]);
                Files.write(Paths.get("resources/Playlists.json"), obj.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 4 && args[1].equals("config")) {
            try {
                JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Config.json"))));
                obj.put(args[2], args[3]);
                Files.write(Paths.get("resources/Config.json"), obj.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.prefix + "save");
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
