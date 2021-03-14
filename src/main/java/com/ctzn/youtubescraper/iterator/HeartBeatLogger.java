package com.ctzn.youtubescraper.iterator;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class HeartBeatLogger {

    private final int logPeriod; //ms

    private long timeRef;
    private boolean doInit = true;

    public HeartBeatLogger(int logPeriod) {
        this.logPeriod = logPeriod;
    }

    public void run(Logger logger, Level level, Supplier<String> supplier) {
        long now = System.currentTimeMillis();
        if (doInit) {
            timeRef = now;
            doInit = false;
            return;
        }
        if (now - timeRef > logPeriod) {
            timeRef = now;
            if (logger.isLoggable(INFO)) logger.log(level, supplier);
        }
    }

}
