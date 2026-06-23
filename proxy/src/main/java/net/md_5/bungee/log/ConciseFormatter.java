package net.md_5.bungee.log;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

@RequiredArgsConstructor
public class ConciseFormatter extends Formatter {

    private final boolean colored;
    private final DateFormat date;

    @Override
    public String format(LogRecord record) {
        StringBuilder formatted = new StringBuilder();

        formatted.append(date.format(record.getMillis()));
        formatted.append(" [");
        formatted.append(record.getLevel().getLocalizedName());
        formatted.append("] ");
        String message = formatMessage(record);
        if (!colored) {
            message = ChatColor.stripColor(message);
        }
        formatted.append(message);
        formatted.append('\n');
        if (record.getThrown() != null) {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            formatted.append(writer);
        }

        return formatted.toString();
    }
}
