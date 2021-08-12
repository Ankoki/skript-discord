package com.ankoki.skriptdiscord.api.managers;

import ch.njol.skript.Skript;
import com.ankoki.skriptdiscord.api.DiscordBot;
import com.ankoki.skriptdiscord.utils.Console;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BotManager {

    private static final Map<String, DiscordBot> DISCORD_BOT_MAP = new ConcurrentHashMap<>();

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

    public static void disable(String bot) {
        disable(DISCORD_BOT_MAP.get(bot));
    }

    public static void disable(DiscordBot bot) {
        if (bot == null) return;
        bot.getJda().shutdown();
        Console.log("The bot " + bot.getName() + " just got shutdown.");
        DISCORD_BOT_MAP.remove(bot.getName());
    }

    public static void disableAll() {
        DISCORD_BOT_MAP.forEach((name , bot) -> {
            BotManager.disable(bot);
        });
    }

    public static boolean isEnabled(String bot) {
        return DISCORD_BOT_MAP.containsKey(bot);
    }
}
