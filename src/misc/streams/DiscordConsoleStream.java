package misc.streams;

import net.dv8tion.jda.entities.TextChannel;

import java.io.PrintStream;

/**
 * Created by TheWithz on 2/14/16.
 */
public class DiscordConsoleStream {
    private TextChannel channel;
    private RedirectStream systemOut;
    private RedirectStream systemErr;

    public DiscordConsoleStream(TextChannel channel) {
        this(channel, false);
    }

    public DiscordConsoleStream(TextChannel channel, boolean enable) {
        setChannel(channel);
        systemOut = new RedirectStream(System.out) {
            @Override
            protected void enableRedirect(boolean enable) {
                if (enable) {
                    System.setOut(this);
                } else {
                    System.setOut(getOut());
                }
            }
        };
        systemErr = new RedirectStream(System.err) {
            @Override
            protected void enableRedirect(boolean enable) {
                if (enable) {
                    System.setErr(this);
                } else {
                    System.setErr(getOut());
                }
            }
        };
        enableRedirect(enable);
    }

    public void print(String string) {
        systemOut.print(string);
    }

    public void println(String string) {
        systemOut.println(string);
    }

    public void println() {
        systemOut.println();
    }

    public TextChannel getChannel() {
        return channel;
    }

    public void setChannel(TextChannel channel) {
        if (channel == null) throw new NullPointerException("Cannot redirect Console output to a null TextChannel!");
        this.channel = channel;
    }

    public void enableRedirect(boolean enable) {
        systemOut.enableRedirect(enable);
        systemErr.enableRedirect(enable);
    }

    private void printToDiscord(String s) {
        channel.sendMessage(s);
    }

    private abstract class RedirectStream extends PrintStream {

        public RedirectStream(PrintStream out) {
            super(out);
        }

        protected abstract void enableRedirect(boolean enable);

        @Override
        public void println() {
            ((PrintStream) out).println();
            DiscordConsoleStream.this.printToDiscord("\n");
        }

        @Override
        public void println(String s) {
            s = s == null ? "null" : s;
            ((PrintStream) out).println(s);
            DiscordConsoleStream.this.printToDiscord(s + "\n");
        }

        @Override
        public void print(String s) {
            s = s == null ? "null" : s;
            ((PrintStream) out).print(s);
            DiscordConsoleStream.this.printToDiscord(s);
        }

        public PrintStream getOut() {
            return (PrintStream) out;
        }
    }
}