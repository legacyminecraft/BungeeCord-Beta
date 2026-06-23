package net.md_5.bungee.log;

import net.md_5.bungee.BungeeCord;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class BungeeLogger extends Logger {

    private final LogDispatcher dispatcher = new LogDispatcher(this);

    public BungeeLogger(BungeeCord bungee) {
        super("BungeeCord", null);
        setUseParentHandlers(false);

        try {
            FileHandler fileHandler = new FileHandler("proxy.log", true);
            fileHandler.setFormatter(new ConciseFormatter(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
            addHandler(fileHandler);

            ColouredWriter consoleHandler = new ColouredWriter(bungee.getConsoleReader());
            consoleHandler.setFormatter(new ConciseFormatter(true, new SimpleDateFormat("HH:mm:ss")));
            addHandler(consoleHandler);
        } catch (IOException ex) {
            System.err.println("Could not register logger!");
            ex.printStackTrace();
        }
        dispatcher.start();
    }

    @Override
    public void log(LogRecord record) {
        dispatcher.queue(record);
    }

    void doLog(LogRecord record) {
        super.log(record);
    }
}
