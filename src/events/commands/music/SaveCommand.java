package events.commands.music;

import bots.RunBot;
import events.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONException;
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
        if (RunBot.OpRequired(event))
            return;

        RunBot.checkArgs(args, 1, ":x: No json was specified to save to. See " + RunBot.PREFIX + "help " + getAliases().get(0));
        RunBot.checkArgs(args, 2, ":x: No key was specified to save as. See " + RunBot.PREFIX + "help " + getAliases().get(0));
        RunBot.checkArgs(args, 3, ":x: No content was specified to save. See " + RunBot.PREFIX + "help " + getAliases().get(0));

        switch (args[0]) {
            case RunBot.PREFIX + "jsonSave":
                handleSave(event, args);
                break;
            case RunBot.PREFIX + "jsonRemove":
                break;
        }

    }

    private void handleSave(MessageReceivedEvent event, String[] args) {
        switch (args[1]) {
            case "latex":
                handleLatex(event, args);
                break;
            case "playlist":
                handlePlaylist(event, args);
                break;
            case "config":
                if (!event.getAuthor().getId().equals("122764399961309184")) {
                    sendMessage(event, ":x: Only " + event.getAuthor().getAsMention() + " can update the config");
                    return;
                }
                handleConfig(event, args);
                break;
            default:
                sendMessage(event, ":x: Unknown Action argument: `" + args[1] + "` was provided. " +
                        "Please use `" + RunBot.PREFIX + "help " + getAliases().get(0) + "` for more information.");
                break;
        }
    }

    private void handleLatex(MessageReceivedEvent event, String[] args) {
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/commonLatex.json"))));
            if (checkKeyExists(obj, event, args))
                return;
            obj.put(args[2], args[3]);
            Files.write(Paths.get("resources/commonLatex.json"), obj.toString().getBytes());
            sendMessage(event, ":white_check_mark: The JSON file was updated successfully.");
        } catch (IOException e) {
            sendMessage(event, ":x: " + e.getMessage());
        }
    }

    private void handlePlaylist(MessageReceivedEvent event, String[] args) {
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Playlists.json"))));
            if (checkKeyExists(obj, event, args))
                return;
            obj.put(args[2], args[3]);
            Files.write(Paths.get("resources/Playlists.json"), obj.toString().getBytes());
            sendMessage(event, ":white_check_mark: The JSON file was updated successfully.");
        } catch (IOException e) {
            sendMessage(event, ":x: " + e.getMessage());
        }
    }

    private void handleConfig(MessageReceivedEvent event, String[] args) {
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("resources/Config.json"))));
            if (checkKeyExists(obj, event, args))
                return;
            obj.put(args[2], args[3]);
            Files.write(Paths.get("resources/Config.json"), obj.toString().getBytes());
            sendMessage(event, ":white_check_mark: The JSON file was updated successfully.");
        } catch (IOException e) {
            sendMessage(event, ":x: " + e.getMessage());
        }
    }

    private boolean checkKeyExists(JSONObject obj, MessageReceivedEvent event, String[] args) {
        try {
            obj.getString(args[2]);
        } catch (JSONException e) {
            return false;
        }
        sendMessage(event, ":x: The key you provided already has a corresponding object!");
        return true;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(RunBot.PREFIX + "jsonSave", RunBot.PREFIX + "jsonRemove");
    }

    @Override
    public String getDescription() {
        return "Stores a value and a key into a designated .json file for later use";
    }

    @Override
    public String getName() {
        return "Save Command";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Arrays.asList(RunBot.PREFIX + "save <json file> <key> <value>", "Example : " + RunBot.PREFIX + "save playlist 80smix <url" +
                " of playlist>");
    }
}
