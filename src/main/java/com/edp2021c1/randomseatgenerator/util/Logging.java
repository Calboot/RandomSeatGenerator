/*
 * RandomSeatGenerator
 * Copyright (C) 2023  EDP2021C1
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.edp2021c1.randomseatgenerator.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Formatter;
import java.util.logging.*;

/**
 * Logging related util.
 *
 * @author Calboot
 * @since 1.4.4
 */
public class Logging {
    /**
     * Logger.
     */
    public static final Logger LOG = Logger.getLogger("RandomSeat");
    private static final Path LOG_DIR = Paths.get(MetaData.DATA_DIR, "logs");
    private static final Path[] LOG_PATHS;
    private static final MessageFormat MESSAGE_FORMAT = new MessageFormat("[{0,date,HH:mm:ss}] [{1}/{2}] {3}\n");
    private static final Formatter DEFAULT_FORMATTER;
    private static final Map<Long, Thread> threadIDMap = new HashMap<>();
    private static boolean started = false;

    static {
        String str = "%tF-%%d.log".formatted(new Date());
        int t = 1;
        while (Files.exists(LOG_DIR.resolve(str.formatted(t)))) {
            t++;
        }
        LOG_PATHS = new Path[]{LOG_DIR.resolve("latest.log"), LOG_DIR.resolve(str.formatted(t))};

        DEFAULT_FORMATTER = new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage();
            }
        };
    }

    /**
     * Starts logging.
     */
    public static void start() {
        if (started) {
            throw new IllegalStateException("Logging already started");
        }

        started = true;

        LOG.setLevel(Level.ALL);
        LOG.setUseParentHandlers(false);
        LOG.setFilter(record -> {
            record.setMessage(format(record));
            return true;
        });

        try {
            if (Files.isRegularFile(LOG_DIR)) {
                Files.delete(LOG_DIR);
            }
            Files.createDirectories(LOG_DIR);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create randomseat.log", e);
        }

        for (final Handler h : getHandlers()) {
            LOG.addHandler(h);
        }

        LOG.info("Logging started");
        LOG.info("Log files: " + Arrays.toString(LOG_PATHS));
    }

    private static String format(LogRecord record) {
        String message = record.getMessage();

        final StringBuffer buffer = new StringBuffer(1024);

        MESSAGE_FORMAT.format(new Object[]{
                new Date(record.getMillis()),
                getThreadById(record.getLongThreadID()).getName(), record.getLevel().getName(),
                message
        }, buffer, null);

        return buffer.toString();
    }

    private static ArrayList<Handler> getHandlers() {
        ArrayList<Handler> handlers = new ArrayList<>(3);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(DEFAULT_FORMATTER);
        consoleHandler.setLevel(Level.FINER);
        handlers.add(consoleHandler);

        try {
            for (final Path path : LOG_PATHS) {
                FileHandler latestLogHandler = new FileHandler(path.toString());
                latestLogHandler.setLevel(Level.FINEST);
                latestLogHandler.setFormatter(DEFAULT_FORMATTER);
                latestLogHandler.setEncoding("UTF-8");
                handlers.add(latestLogHandler);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return handlers;
    }

    private static Thread getThreadById(long id) {
        if (threadIDMap.containsKey(id)) {
            return threadIDMap.get(id);
        }

        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (final Thread t : threads) {
            threadIDMap.put(t.getId(), t);
            if (t.getId() == id) {
                return t;
            }
        }
        throw new RuntimeException("Thread not found");
    }
}