package enums;

/**
 * Created by thewithz on 1/8/2016.
 */
public enum ChatGroup {
    GENERAL("147158335387336705"),
    CHESS("147169092241981440"),
    STSL("147169161984868352"),
    BOT("147169039049949184");

    String CHAT_ID;

    ChatGroup(String id) {
        CHAT_ID = id;
    }

    public String getID() {
        return CHAT_ID;
    }
}

