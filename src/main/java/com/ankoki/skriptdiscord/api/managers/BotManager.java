package com.ankoki.skriptdiscord.api.managers;

import ch.njol.skript.Skript;
import com.ankoki.skriptdiscord.api.DiscordBot;

import java.util.HashMap;
import java.util.Map;

public class BotManager {

    private static final Map<String, DiscordBot> DISCORD_BOT_MAP = new HashMap<>();

    public static void registerBot(DiscordBot bot) {
        if (DISCORD_BOT_MAP.containsKey(bot.getName())) {
            Skript.warning("A bot with this name (" + bot.getName() + ") already exists! It will be replaced");
        }
        DISCORD_BOT_MAP.put(bot.getName(), bot);
    }

    public static DiscordBot getFirstBot() {
        DiscordBot[] array = DISCORD_BOT_MAP.values().toArray(new DiscordBot[0]);
        return array.length >= 1 ? array[0] : null;
    }
}
