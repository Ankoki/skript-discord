package com.ankoki.skriptdiscord.utils;

import com.ankoki.skriptdiscord.SkriptDiscord;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;

import java.util.Arrays;

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

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(SkriptDiscord.getInstance(), runnable);
    }

    public static void throwException(Exception ex) {
        Console.error("Hmmm, something went wrong. Please contact Ankoki#0001 on discord or join our support discord.");
        Console.error("Please provide us with the whole error so we can solve your issue.");
        Console.error("Join Aspect Productions here: discord.gg/3RWFg2xDBF");
        ex.printStackTrace();
    }
}