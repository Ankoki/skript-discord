package com.ankoki.skriptdiscord.utils;

import net.dv8tion.jda.api.requests.GatewayIntent;

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
        return (command.substring(0, (splitCommand[0] + " ").length())).split(" ");
    }
}