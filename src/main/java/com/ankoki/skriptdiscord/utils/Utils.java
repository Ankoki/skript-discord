package com.ankoki.skriptdiscord.utils;

import com.ankoki.skriptdiscord.SkriptDiscord;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;

public final class Utils {
    private Utils(){}

    public static GatewayIntent getGatewaySafely(String s) {
        try {
            return GatewayIntent.valueOf(s);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static String getCommandName(String command, String prefix) {
        String[] splitCommand = command.split(" ");
        return splitCommand[0].substring(prefix.length());
    }

    public static String[] getCommandArguments(String command) {
        String[] splitCommand = command.split(" ");
        try {
            return (command.substring(0, (splitCommand[0] + " ").length())).split(" ");
        } catch (IndexOutOfBoundsException ex) {
            return (command.substring(0, (splitCommand[0] + " ").length() - 1)).split(" ");
        }
    }

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(SkriptDiscord.getInstance(), runnable);
    }
}