package com.ankoki.skriptdiscord.utils;

import com.ankoki.skriptdiscord.SkriptDiscord;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

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
        if (command.isEmpty()) return new String[0];
        String[] splitCommand = command.split(" ");
        return Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
    }

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(SkriptDiscord.getInstance(), runnable);
    }
}