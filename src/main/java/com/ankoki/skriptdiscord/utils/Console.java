package com.ankoki.skriptdiscord.utils;

import com.ankoki.skriptdiscord.SkriptDiscord;

import java.util.logging.Logger;

public final class Console {
    private Console(){}

    private static final String PREFIX = "§7skript-discord §8| §r";
    private static final Logger LOGGER = SkriptDiscord.getInstance().getLogger();;

    public static void log(Object message) {
        LOGGER.info(PREFIX + message);
    }

    public static void warning(Object message) {
        LOGGER.warning(PREFIX + message);
    }

    public static void debug(Object message) {
        LOGGER.info("§7skript-discord §eDEUBG §8| §r" + message);
    }

    public static void error(Object message) {
        LOGGER.severe(PREFIX + "§c" + message);
    }
}