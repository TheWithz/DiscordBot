package enums;

/**
 * Created by thewithz on 1/8/2016.
 */
public enum DiscordUser {
    MARC("122763729061412866", "marc"),
    ALBERT("122766571213946880", "albert"),
    ALEX("122771361910358016", "alex"),
    BOT("129776230999392256", "bot"),
    CHASE("126102141742874624", "chase"),
    COKES("113408334098403328", "cokes"),
    JAMES("122763813802999808", "james"),
    KIAN("122813846078357505", "kian"),
    MAX("122763722564304897", "max"),
    NATHAN("122764399961309184", "nathan"),
    NICK("133707698473533440", "nick"),
    SALAAM("133547083318689792", "salaam"),
    GERRY("139206350671577088", "gerry"),
    BILLCHEU("139129596313337856", "bill"),
    JCHRELLS("113409312449126400", "jchrells"),
    BORIS("138326825360293888", "boris"),
    JEFFLEE("140514662922846209", "jeff"),
    BEN("141010289243062282", "ben"),
    MAXIMUM("138468476674113537", "edward"),
    BRIAN("137727811984162818", "brian");

    String USER_ID;
    String USER_NAME;

    DiscordUser(String id, String name) {
        USER_ID = id;
        USER_NAME = name;
    }

    public String getID() {
        return USER_ID;
    }

    public String getName() {
        return USER_NAME;
    }

}
