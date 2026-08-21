package net.md_5.bungee.log;

import net.kyori.ansi.ANSIComponentRenderer;
import net.kyori.ansi.StyleOps;
import net.md_5.bungee.api.ChatColor;
import org.jline.reader.LineReader;
import org.jspecify.annotations.Nullable;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ColouredWriter extends Handler {

    private final LineReader lineReader;

    public ColouredWriter(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    public void print(String s) {
        lineReader.printAbove(chatColorsToAnsi(s));
    }

    @Override
    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            print(getFormatter().format(record));
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    private static String chatColorsToAnsi(String text) {
        ANSIComponentRenderer.ToString<ChatColor> renderer = ANSIComponentRenderer.toString(ChatColorStyle.instance);
        ChatColor lastColor = ChatColor.WHITE;
        renderer.pushStyle(lastColor);

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\u00A7' && i < text.length() - 1) {
                ChatColor color = ChatColor.getByChar(text.charAt(i + 1));
                if (color != null) {
                    renderer.popStyle(lastColor);
                    lastColor = color;
                    renderer.pushStyle(lastColor);
                    i++;
                    continue;
                }
            }

            renderer.text(String.valueOf(ch));
        }

        renderer.popStyle(lastColor);
        renderer.complete();
        return renderer.asString();
    }

    private static final class ChatColorStyle implements StyleOps<ChatColor> {
        private static final ChatColorStyle instance = new ChatColorStyle();

        @Override
        public int color(ChatColor color) {
            return switch (color) {
                case BLACK -> 0x000000;
                case DARK_BLUE -> 0x0000AA;
                case DARK_GREEN -> 0x00AA00;
                case DARK_AQUA -> 0x00AAAA;
                case DARK_RED -> 0xAA0000;
                case DARK_PURPLE -> 0xAA00AA;
                case GOLD -> 0xFFAA00;
                case GRAY -> 0xAAAAAA;
                case DARK_GRAY -> 0x555555;
                case BLUE -> 0x5555FF;
                case GREEN -> 0x55FF55;
                case AQUA -> 0x55FFFF;
                case RED -> 0xFF5555;
                case LIGHT_PURPLE -> 0xFF55FF;
                case YELLOW -> 0xFFFF55;
                default -> 0xFFFFFF;
            };
        }

        @Override
        public State bold(ChatColor color) {
            return State.UNSET;
        }

        @Override
        public State italics(ChatColor color) {
            return State.UNSET;
        }

        @Override
        public State underlined(ChatColor color) {
            return State.UNSET;
        }

        @Override
        public State strikethrough(ChatColor color) {
            return State.UNSET;
        }

        @Override
        public State obfuscated(ChatColor color) {
            return State.UNSET;
        }

        @Override
        public @Nullable String font(ChatColor color) {
            return null;
        }
    }
}
