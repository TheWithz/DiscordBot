package events.commands.music;

/**
 * Created by thewithz on 7/3/16.
 */
public enum Symbols {
    PLAY("▶"),
    PAUSE(":pause_button:"),
    REPEAT(":repeat_one:"),
    SHUFFLE(":twisted_rightwards_arrows:"),
    CURRENT("\uD83D\uDD18"),
    LOW_VOLUME("\uD83D\uDD08"),
    MED_VOLUME("\uD83D\uDD09"),
    LOUD_VOLUME("\uD83D\uDD0A"),
    MUTE("\uD83D\uDD07"),
    SPACE("▬");

    private String value;

    Symbols(String input) {
        this.value = input;
    }

    public String toString() {
        return value;
    }

}
