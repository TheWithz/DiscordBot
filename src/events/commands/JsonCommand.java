package events.commands;

import bots.RunBot;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TheWithz on 4/29/16.
 */
public class JsonCommand extends Command {
    private boolean allowRemove;
    private String fileToRemove;
    private HashMap<String, File> jsonFiles;

    public JsonCommand() {
        allowRemove = false;
        fileToRemove = "";
        jsonFiles = new HashMap<>();

        File file = new File(".");
        Collection<File> files = FileUtils.listFiles(file, null, false);
        files.stream()
             .filter(file1 -> file1.getName().length() >= 6)
             .filter(file1 -> file1.getName().contains(".json"))
             .filter(file1 -> !file1.getName().contains("Config"))
             .forEach(file1 -> jsonFiles.put(file1.getName(), file1));
    }

    //todo 5/7/16 finish this!
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (RunBot.OpRequired(event))
            return;

        RunBot.checkArgs(args, 1, ":x: No json action argument was specified. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        switch (args[1]) {
            case "save":
            case "add":
                handleSaveToJson(event, args);
                break;
            case "remove":
                if (RunBot.OwnerRequired(event))
                    return;
                handleRemoveFromJson(event, args);
                break;
            case "delete":
                if (RunBot.OwnerRequired(event))
                    return;
                handleDeleteJsonFile(event, args);
                break;
            case "new":
            case "create":
                if (RunBot.OwnerRequired(event))
                    return;
                handleMakeNewJson(event, args);
                break;
            case "show":
                handleShowJsonContents(event, args);
                break;
            case "list":
                handleListJsonFiles(event);
                break;
            default:
                event.getChannel().sendMessage(":x: Unknown Action argument: `" + args[1] + "` was provided. " +
                                                       "Please use `" + RunBot.PREFIX + "help " + getAliases().get(0) + "` for more information.").queue();
                break;
        }

    }

    private void handleListJsonFiles(MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("```md\n");
        jsonFiles.forEach((key, value) -> builder.append("<").append(key).append(">\n"));
        event.getChannel().sendMessage(builder.append("```").toString()).queue();
    }

    private void handleShowJsonContents(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No json file was specified to show. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        if (args[2].equals("Config.json")) {
            if (RunBot.OwnerRequired(event)) {
                event.getChannel().sendMessage(":name_badge::name_badge::name_badge: You should know better... :name_badge::name_badge::name_badge:").queue();
                return;
            }
        }

        StringBuilder builder = new StringBuilder();
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get(args[2]))));
            builder.append("```md\n");
            for (Object object : obj.keySet()) {
                builder.append("<")
                       .append(object.toString())
                       .append("> : <")
                       .append(obj.get(object.toString()))
                       .append(">\n________________________________________________\n");
            }
            builder.append("```");
            event.getChannel().sendMessage(builder.toString()).queue();
        } catch (IOException e) {
            event.getChannel().sendMessage(":x: The specified file to show `" + args[2] + "` does not exist.").queue();
        }
    }

    private void handleDeleteJsonFile(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No json file was specified to delete. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        if (!allowRemove) {
            event.getChannel()
                 .sendMessage(":name_badge: :name_badge: :name_badge: Are you sure you want to permanently delete the json file " + args[2] + " If so, run the command again" +
                                      ". :name_badge: :name_badge: :name_badge:").queue();
            allowRemove = true;
            fileToRemove = args[2];
            return;
        }

        try {
            if (!args[2].equals(fileToRemove)) {
                event.getChannel()
                     .sendMessage(":name_badge: :name_badge: :name_badge: Be careful!! you almost permanently deleted the json file " + args[2] + " by accident! :name_badge: " +
                                          ":name_badge: :name_badge:").queue();
                allowRemove = false;
                fileToRemove = "";
                return;
            }
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get(args[2]))));
            BashCommand.runLinuxCommand("rm " + args[2]);
            jsonFiles.remove(args[2]);
            event.getChannel().sendMessage(":white_check_mark: the json file " + args[2] + " was successfully deleted.").queue();
        } catch (IOException e) {
            event.getChannel().sendMessage(":x: The specified file to delete `" + args[2] + "` does not exist.").queue();
            allowRemove = false;
        }

    }

    private void handleMakeNewJson(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No json was specified to create. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        if (jsonFiles.containsKey(args[2])) {
            event.getChannel().sendMessage(":x: A json file `" + args[2] + "` already exists.").queue();
            return;
        }

        if (!args[2].contains(".json")) {
            event.getChannel().sendMessage(":x: A json file needs to have the `.json` extension.").queue();
            return;
        }

        if (args[2].contains("\\") || args[2].contains("/")) {
            event.getChannel().sendMessage(":x: Nice try. :joy:").queue();
            return;
        }

        JSONObject obj = new JSONObject();
        try {
            Files.write(Paths.get(args[2]), obj.toString(4).getBytes());
            jsonFiles.put(args[2], new File(args[2]));
            event.getChannel().sendMessage(":white_check_mark: new json `" + args[2] + "` was generated successfully.").queue();
        } catch (IOException e1) {
            event.getChannel().sendMessage(":x: new json `" + args[2] + "` was **not** generated successfully.").queue();
        }
    }

    private void handleRemoveFromJson(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No json was specified to remove from. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);
        RunBot.checkArgs(args, 3, ":x: No key was specified to remove. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get(args[2]))));
            if (!checkKeyExists(obj, args[3])) {
                event.getChannel().sendMessage(":x: The key you provided does not exist!").queue();
                return;
            }
            obj.remove(args[3]);
            Files.write(Paths.get(args[2]), obj.toString().getBytes());
            event.getChannel().sendMessage(":white_check_mark: " + args[2] + " was updated successfully by removing " + args[3]).queue();
        } catch (IOException e) {
            event.getChannel().sendMessage(":x: The specified file to remove from `" + args[2] + "` does not exist.").queue();
        }
    }

    private void handleSaveToJson(MessageReceivedEvent event, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No json was specified to save to. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);
        RunBot.checkArgs(args, 3, ":x: No key was specified to save as. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);
        RunBot.checkArgs(args, 4, ":x: No content was specified to save. See " + RunBot.PREFIX + "help " + getAliases().get(0), event);

        if (args[2].equals("Config.json")) {
            if (RunBot.OwnerRequired(event))
                return;
        }

        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get(args[2]))));
            if (checkKeyExists(obj, args[3])) {
                event.getChannel().sendMessage(":x: The key you provided already has a corresponding object!").queue();
                return;
            }
            obj.put(args[3], args[4]);
            Files.write(Paths.get(args[2]), obj.toString().getBytes());
            event.getChannel().sendMessage(":white_check_mark: " + args[2] + " was updated successfully by adding " + args[3]).queue();
        } catch (IOException e) {
            event.getChannel().sendMessage(":x: The specified file to save to `" + args[2] + "` does not exist.").queue();
        }
    }

    private boolean checkKeyExists(JSONObject obj, String key) {
        try {
            obj.getString(key);
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
