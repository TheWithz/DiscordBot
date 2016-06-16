package events.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thewithz on 6/16/16.
 */
public class ArgParse {

    private class FSM {

        private char[] input;
        private int index;
        private ArrayList<String> tokens;
        private StringBuilder acc;
        private boolean isQuoted;

        FSM(char[] input) {
            this.input = input;
            this.tokens = new ArrayList<>();
            this.acc = new StringBuilder();
        }

        private boolean matches(String str) {
            if (index + str.length() > input.length) {
                return false;
            }

            for (int i = 0; i < str.length(); i++) {
                if (input[index + i] != str.charAt(i)) {
                    return false;
                }
            }

            return true;
        }

        private void endToken() {
            String token = acc.toString();
            acc.setLength(0);
            if (token.length() > 0) {
                tokens.add(token);
            }
        }

        private void pass() {
            if (matches("\\\"")) {
                acc.append("\"");
                index += 2;
            } else if (matches("\"")) {
                isQuoted = !isQuoted;
                index++;
            } else if (!isQuoted && matches(" ")) {
                endToken();
                index++;
            } else {
                acc.append(input[index]);
                index++;
            }
        }

        void execute() {
            while (index < input.length) {
                pass();
            }
            endToken();
        }

        List<String> getTokens() {
            return tokens;
        }

    }

    public String[] parse(String string) {
        FSM fsm = new FSM(string.toCharArray());
        fsm.execute();
        return fsm.getTokens().toArray(new String[0]);
    }


}
