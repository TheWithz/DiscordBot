package events.commands;

import bots.RunBot;
import misc.Database;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by NathanWithz on 6/23/2016.
 */
public class TagCommand extends Command {

    //Database Methods
    public static final String ADD_TAG = "addTag";
    public static final String EDIT_TAG_LABEL = "editTagLabel";
    public static final String EDIT_TAG_CONTENT = "editTagContent";
    public static final String GET_TAG = "getTag";
    public static final String GET_TAGS = "getTags";
    public static final String REMOVE_TAG = "removeTag";

    private HashMap<String, Tag> tags = new HashMap<>();

    public TagCommand() {
        try {
            ResultSet sqlTags = Database.getInstance().getStatement(GET_TAGS).executeQuery();
            while (sqlTags.next()) {
                String label = sqlTags.getString(2);
                Tag tag = new Tag(
                        sqlTags.getInt(1),     //Id
                        label,  //label
                        sqlTags.getString(3)  //Content
                );
                tags.put(label, tag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        try {
            RunBot.checkArgs(args, 1, ":x: No Action argument was provided. Please use `" + RunBot.PREFIX + "help " + getAliases().get(0) + "` for more information.", e);

            switch (args[1].toLowerCase()) {
                case "show":
                    handleShow(e, args);
                    break;
                case "create":
                case "add":
                    handleCreate(e, args);
                    break;
                case "delete":
                case "remove":
                    handleDelete(e, args);
                    break;
                case "edit":
                    handleEdit(e, args);
                    break;
                case "list":
                case "print":
                    handleList(e);
                    break;
                case "owner":
                    handleOwner(e, args);
                    break;
                default:
                    sendMessage(e, ":x: Unknown Action argument: `" + args[1] + "` was provided. " +
                            "Please use `" + RunBot.PREFIX + "help " + getAliases().get(0) + "` for more information.");
            }
        } catch (SQLException e1) {
            sendMessage(e, ":x: An SQL error occurred while processing command.\nError Message: " + e1.getMessage());
            e1.printStackTrace();
        } catch (IllegalArgumentException e2) {
            sendMessage(e, e2.getMessage());
        }
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList(RunBot.PREFIX + "tag");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "Tag Command";
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

    private void handleShow(MessageReceivedEvent e, String[] args) {
        RunBot.checkArgs(args, 2, ":x: No TagLabel was specified. Usage: `" + getAliases().get(0) + " show [TagLabel]`", e);

        String label = args[2].toLowerCase();
        Tag tag = tags.get(label);
        if (tag == null) {
            sendMessage(e, ":x: Sorry, `" + label + "` isn't a known tag.");
            return;
        }

        if (tag.content.length() >= 1950) {
            sendMessage(e, "```fix\nShowing tag: [" + tag.label + "]```");
            RunBot.printAsFile(e.getTextChannel(), new StringBuilder(tag.content), tag.label);
        } else
            sendMessage(e, "```fix\nShowing tag: [" + tag.label + "]```" + tag.content);
    }

    private void handleCreate(MessageReceivedEvent e, String[] args) throws SQLException {
        if (RunBot.OpRequired(e))
            return;

        RunBot.checkArgs(args, 2, ":x: No TagLabel for the new tag was provided. Usage: `" + getAliases().get(0) + " create [TagLabel] [Content]`", e);
        RunBot.checkArgs(args, 3, ":x: No Content for the new tag was provided. Usage: `" + getAliases().get(0) + " create [TagLabel] [Content]`", e);

        String label = args[2].toLowerCase();
        String content = StringUtils.join(args, " ", 3, args.length);
        Tag tag = tags.get(label);

        if (tag != null) {
            sendMessage(e, ":x: A tag already exists with the name `" + label + "`.");
            return;
        }

        PreparedStatement addTag = Database.getInstance().getStatement(ADD_TAG);
        addTag.setString(1, label);//Label
        addTag.setString(2, content);//Content

        if (addTag.executeUpdate() == 0)
            throw new SQLException(ADD_TAG + " reported no modified rows!");

        tag = new Tag(Database.getAutoIncrement(addTag, 1), label, content);
        tags.put(label, tag);
        addTag.clearParameters();

        sendMessage(e, ":white_check_mark: Created `" + label + "` tag.");
    }

    private void handleDelete(MessageReceivedEvent e, String[] args) throws SQLException {
        if (RunBot.OpRequired(e))
            return;

        RunBot.checkArgs(args, 2, ":x: No TagLabel was specified. Usage: `" + getAliases().get(0) + " remove [TagLabel]`", e);

        String label = args[2].toLowerCase();
        Tag tag = tags.get(label);
        if (tag == null) {
            sendMessage(e, ":x: Sorry, `" + label + "` isn't a known tag.");
            return;
        }

        PreparedStatement removeTagList = Database.getInstance().getStatement(REMOVE_TAG);
        removeTagList.setInt(1, tag.id);
        //removeTagList.setString(2, tag.label);
        if (removeTagList.executeUpdate() == 0)
            throw new SQLException(REMOVE_TAG + " reported no updated rows!");
        removeTagList.clearParameters();

        tags.remove(label);
        sendMessage(e, ":white_check_mark: Deleted the `" + label + "` tag.");
    }

    private void handleEdit(MessageReceivedEvent e, String[] args) throws SQLException {
        if (RunBot.OpRequired(e))
            return;

        RunBot.checkArgs(args, 2, ":x: No subject was specified. Usage: `" + getAliases().get(0) + " edit [subject] [TagLabel] [Content...]`", e);
        RunBot.checkArgs(args, 3, ":x: No TagLabel was specified. Usage: `" + getAliases().get(0) + " edit [subject] [TagLabel] [Content...]`", e);
        RunBot.checkArgs(args, 4, ":x: No Content was specified. Cannot edit a tag so that it does not exist" +
                "Usage: `" + getAliases().get(0) + " edit [subject] [TagLabel] [Content...]`", e);

        switch (args[2]) {
            case "content":
                handleEditContent(e, args);
                break;
            case "label":
                handleEditLabel(e, args);
                break;
            default:
                sendMessage(e, ":x: Unknown Modifier argument: `" + args[2] + "` was provided. " +
                        "Please use `" + RunBot.PREFIX + "help " + getAliases().get(0) + "` for more information.");
                break;
        }

    }

    private void handleEditLabel(MessageReceivedEvent e, String[] args) throws SQLException {
        String oldLabel = args[3].toLowerCase();
        String newLabel = args[4].toLowerCase();
        Tag tag = tags.get(oldLabel);

        if (tag == null) {
            sendMessage(e, ":x: Sorry, `" + oldLabel + "` isn't a known tag. " +
                    "Try using `" + getAliases().get(0) + " create " + oldLabel + "` to create a new tag by this name.");
            return;
        }

        PreparedStatement editTagEntry = Database.getInstance().getStatement(EDIT_TAG_LABEL);
        editTagEntry.setString(1, newLabel);
        editTagEntry.setInt(2, tag.id);
        editTagEntry.setString(3, oldLabel);
        if (editTagEntry.executeUpdate() == 0)
            throw new SQLException(EDIT_TAG_LABEL + " reported no modified rows!");

        tags.remove(oldLabel);
        tags.put(newLabel, new Tag(
                tag.id, newLabel, tag.content
        ));

        sendMessage(e, ":white_check_mark: Renamed tag `" + oldLabel + "` to '" + newLabel + "'");
    }

    private void handleEditContent(MessageReceivedEvent e, String[] args) throws SQLException {
        String label = args[3].toLowerCase();
        String content = StringUtils.join(args, " ", 4, args.length);
        Tag tag = tags.get(label);

        if (tag == null) {
            sendMessage(e, ":x: Sorry, `" + label + "` isn't a known tag. " +
                    "Try using `" + getAliases().get(0) + " create " + label + "` to create a new tag by this name.");
            return;
        }

        PreparedStatement editTagEntry = Database.getInstance().getStatement(EDIT_TAG_CONTENT);
        editTagEntry.setString(1, content);
        editTagEntry.setInt(2, tag.id);
        editTagEntry.setString(3, label);
        if (editTagEntry.executeUpdate() == 0)
            throw new SQLException(EDIT_TAG_CONTENT + " reported no modified rows!");

        tags.remove(label);
        tags.put(label, new Tag(
                tag.id, label, content
        ));
        tag.content = content;

        sendMessage(e, ":white_check_mark: Edited tag `" + label + "`");
    }

    private void handleList(MessageReceivedEvent e) {
        StringBuilder builder = new StringBuilder();
        builder.append("```fix\nShowing list of tags``````css\n");
        List<String> labels = new ArrayList<>(tags.keySet());
        for (int i = 0; i < tags.keySet().size(); i++) {
            builder.append(i + 1).append(") ").append(labels.get(i)).append("\n");
        }
        sendMessage(e, builder.append("```").toString());
    }

    private void handleOwner(MessageReceivedEvent e, String[] args) {
        //todo: 6/23/16 consider tying ownership to tags
    }

    private static class Tag {
        int id;
        String label;
        String content;

        Tag(int id, String label, String content) {
            this.id = id;
            this.label = label;
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Tag))
                return false;

            Tag te = (Tag) o;
            return te.id == this.id && te.content.equals(this.content);
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public String toString() {
            return "Tag { Id: " + id + "label: " + label + " Content: " + content + "}";
        }
    }

}
