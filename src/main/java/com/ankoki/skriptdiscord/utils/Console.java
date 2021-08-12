package com.ankoki.skriptdiscord.utils;

import com.ankoki.skriptdiscord.SkriptDiscord;

import java.util.logging.Logger;

public final class Console {
    private Console(){}

    private static final String PREFIX = "§7skript-discord §8| §r";
    private static final Logger LOGGER = SkriptDiscord.getInstance().getLogger();;

    public static void log(String message) {
        LOGGER.info(PREFIX + message);
    }

    public static void warning(String message) {
        LOGGER.warning(PREFIX + message);
    }

    public static void debug(String message) {
        LOGGER.info("§7skript-discord §eDEUBG §8| §r" + message);
    }

    public static void error(String message) {
        LOGGER.severe(PREFIX + message);
    }
}