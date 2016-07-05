package events.commands;

import bots.RunBot;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Created by TheWithz on 4/29/16.
 */
public class JsonCommand extends Command {

    //todo 5/7/16 finish this!
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (RunBot.OpRequired(event))
            return;

        RunBot.checkArgs(args, 1, ":x: No json action argument was specified. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        switch (args[1]) {
            case "save":
            case "add":
                handleSave(event, args);
                break;
            case "remove":
            case "delete":
                handleRemove(event, args);
                break;
            case "new":
            case "create":
                handleMakeNewJson(event, args);
                break;
            default:
                sendMessage(event, ":x: Unknown Action argument: `" + args[1] + "` was provided. " +
                        "Please use `" + RunBot.PREFIX + "help " + getAliases().get(0) + "` for more information.");
                break;
        }

    }

    private void handleMakeNewJson(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No json was specified to create. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        JSONObject obj = new JSONObject();
        try {
            Files.write(Paths.get(args[2]), obj.toString(4).getBytes());
            sendMessage(event, ":white_check_mark: new json `" + args[2] + "` was generated successfully.");
        } catch (IOException e1) {
            sendMessage(event, ":x: new json `" + args[2] + "` was **not** generated successfully.```" + e1.getMessage() + "```");
        }
    }

    private void handleRemove(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No json was specified to remove from. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);
        RunBot.checkArgs(args, 3, ":x: No key was specified to remove. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        if (args[2].equals("Config.json")) {
            if (RunBot.OwnerRequired(event))
                return;
        }

        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get(args[2]))));
            if (!checkKeyExists(obj, args)) {
                sendMessage(event, ":x: The key you provided does not exist!");
                return;
            }
            obj.remove(args[3]);
            Files.write(Paths.get(args[2]), obj.toString().getBytes());
            sendMessage(event, ":white_check_mark: " + args[2] + " was updated successfully by removing " + args[3]);
        } catch (IOException e) {
            sendMessage(event, ":x: " + e.getMessage());
        }
    }

    private void handleSave(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No json was specified to save to. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);
        RunBot.checkArgs(args, 3, ":x: No key was specified to save as. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);
        RunBot.checkArgs(args, 4, ":x: No content was specified to save. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        if (args[2].equals("Config.json")) {
            if (RunBot.OwnerRequired(event))
                return;
        }

        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get(args[2]))));
            if (checkKeyExists(obj, args)) {
                sendMessage(event, ":x: The key you provided already has a corresponding object!");
                return;
            }
            obj.put(args[3], args[4]);
            Files.write(Paths.get(args[2]), obj.toString().getBytes());
            sendMessage(event, ":white_check_mark: " + args[2] + " was updated successfully by adding " + args[3]);
        } catch (IOException e) {
            sendMessage(event, ":x: " + e.getMessage());
        }
    }

    private boolean checkKeyExists(JSONObject obj, String[] args) {
        try {
            obj.getString(args[2]);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "json");
    }

    @Override
    public String getDescription() {
        return "Configures json files for the bot";
    }

    @Override
    public String getName() {
        return "Json Command";
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
        return null;
    }
}
