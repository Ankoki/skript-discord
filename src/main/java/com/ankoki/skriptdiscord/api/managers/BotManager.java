package com.ankoki.skriptdiscord.api.managers;

import com.ankoki.skriptdiscord.api.DiscordBot;

import java.util.HashMap;
import java.util.Map;

public class BotManager {

    private static final Map<String, DiscordBot> DISCORD_BOT_MAP = new HashMap<>();

    public static void registerBot(DiscordBot bot) {
        DISCORD_BOT_MAP.put(bot.getName(), bot);
    }

    public static DiscordBot getFirstBot() {
        DiscordBot[] array = DISCORD_BOT_MAP.values().toArray(new DiscordBot[0]);
        return array.length >= 1 ? array[0] : null;
    }
}
