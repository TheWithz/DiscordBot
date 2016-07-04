package misc;

import events.commands.TagCommand;
import events.commands.TodoCommand;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by TheWithz on 2/21/16.
 */
public class Database {
    private static Database instance;

    private Connection conn;
    private HashMap<String, PreparedStatement> preparedStatements;

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    private Database() {
        preparedStatements = new HashMap();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:thewithz.db");
            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);
            statement.execute("PRAGMA foreign_keys = ON");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Ops(id)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                                            "TodoLists(" +
                                            "id INTEGER," +
                                            "label VARCHAR(50) NOT NULL," +
                                            "owner VARCHAR(18) NOT NULL," +
                                            "locked BOOLEAN," +
                                            "PRIMARY KEY (id)" +
                                            ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                                            "TodoEntries(" +
                                            "id INTEGER," +
                                            "listId INTEGER," +
                                            "content TEXT NOT NULL," +
                                            "checked BOOLEAN," +
                                            "PRIMARY KEY (id)," +
                                            "FOREIGN KEY (listId) REFERENCES TodoLists(id) ON DELETE CASCADE" +
                                            ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                                            "TodoUsers(" +
                                            "listId INT," +
                                            "userId VARCHAR(18) NOT NULL," +
                                            "PRIMARY KEY (listId, userId)," +
                                            "FOREIGN KEY (listId) REFERENCES TodoLists(id) ON DELETE CASCADE" +
                                            ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                                            "Tags(" +
                                            "id INTEGER," +
                                            "label TEXT NOT NULL," +
                                            "content TEXT NOT NULL," +
                                            "PRIMARY KEY (id)" +
                                            ")");

            //Permissions
            preparedStatements.put(Permissions.ADD_OP, conn.prepareStatement("REPLACE INTO Ops (id) VALUES (?)"));
            preparedStatements.put(Permissions.GET_OPS, conn.prepareStatement("SELECT id FROM Ops"));
            preparedStatements.put(Permissions.REMOVE_OPS, conn.prepareStatement("DELETE FROM Ops WHERE id = ?"));

            //TodoCommand
            preparedStatements.put(TodoCommand.ADD_TODO_LIST, conn.prepareStatement("INSERT INTO TodoLists (label, owner, locked) VALUES (?, ?, ?)"));
            preparedStatements.put(TodoCommand.ADD_TODO_ENTRY, conn.prepareStatement("INSERT INTO TodoEntries (listId, content, checked) VALUES (?, ?, ?)"));
            preparedStatements.put(TodoCommand.ADD_TODO_USER, conn.prepareStatement("INSERT INTO TodoUsers (listId, userId) VALUES (?, ?)"));
            preparedStatements.put(TodoCommand.EDIT_TODO_ENTRY, conn.prepareStatement("UPDATE TodoEntries SET content = ? WHERE listId = ? AND id = ?"));
            preparedStatements.put(TodoCommand.GET_TODO_LISTS, conn.prepareStatement("SELECT id, label, owner, locked FROM TodoLists"));
            preparedStatements.put(TodoCommand.GET_TODO_ENTRIES, conn.prepareStatement("SELECT id, content, checked FROM TodoEntries WHERE listId = ?"));
            preparedStatements.put(TodoCommand.GET_TODO_USERS, conn.prepareStatement("SELECT userId FROM TodoUsers WHERE listId = ?"));
            preparedStatements.put(TodoCommand.SET_TODO_LIST_LOCKED, conn.prepareStatement("UPDATE TodoListS SET locked = ? WHERE id = ?"));
            preparedStatements.put(TodoCommand.SET_TODO_ENTRY_CHECKED, conn.prepareStatement("UPDATE TodoEntries SET checked = ? WHERE id = ?"));
            preparedStatements.put(TodoCommand.SET_TODO_ENTRIES_CHECKED, conn.prepareStatement("UPDATE TodoEntries SET checked = ? WHERE listId = ?"));
            preparedStatements.put(TodoCommand.REMOVE_TODO_LIST, conn.prepareStatement("DELETE FROM TodoLists WHERE id = ?"));
            preparedStatements.put(TodoCommand.REMOVE_TODO_ENTRY, conn.prepareStatement("DELETE FROM TodoEntries WHERE id = ?"));
            preparedStatements.put(TodoCommand.REMOVE_TODO_USER, conn.prepareStatement("DELETE FROM TodoUsers WHERE listId = ? AND userId = ?"));

            //TagCommand
            preparedStatements.put(TagCommand.ADD_TAG, conn.prepareStatement("INSERT INTO Tags (label, content) VALUES (?, ?)"));
            preparedStatements.put(TagCommand.EDIT_TAG_LABEL, conn.prepareStatement("UPDATE Tags SET label = ? WHERE id = ? AND label = ?"));
            preparedStatements.put(TagCommand.EDIT_TAG_CONTENT, conn.prepareStatement("UPDATE Tags SET content = ? WHERE id = ? AND label = ?"));
            preparedStatements.put(TagCommand.GET_TAG, conn.prepareStatement("SELECT content FROM Tags WHERE id = ? AND label = ?"));
            preparedStatements.put(TagCommand.GET_TAGS, conn.prepareStatement("SELECT id, label, content FROM Tags"));
            preparedStatements.put(TagCommand.REMOVE_TAG, conn.prepareStatement("DELETE FROM Tags WHERE id = ?"));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getStatement(String statementName) {
        if (!preparedStatements.containsKey(statementName))
            throw new RuntimeException("The statement: '" + statementName + "' does not exist.");
        return preparedStatements.get(statementName);
    }

    public static int getAutoIncrement(PreparedStatement executedStatement, int col) throws SQLException {
        ResultSet autoIncrements = executedStatement.getGeneratedKeys();
        autoIncrements.next();
        return autoIncrements.getInt(col);
    }
}