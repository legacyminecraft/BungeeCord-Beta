package net.md_5.bungee.log;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class LoggingOutputStream extends ByteArrayOutputStream {

    private static final String separator = System.lineSeparator();
    /*========================================================================*/
    private final Logger logger;
    private final Level level;

    @Override
    public void flush() {
        String contents = toString();
        super.reset();

        if (!contents.isEmpty() && contents.endsWith(separator)) {
            contents = contents.substring(0, contents.length() - separator.length());
        }

        if (!contents.isEmpty() && !contents.equals(separator)) {
            logger.logp(level, "", "", contents);
        }
    }
}
