package com.ankoki.skriptdiscord.utils;

import com.ankoki.skriptdiscord.api.BotBuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Utils {

    public static void debug(BotBuilder builder) {
    }

    public static GatewayIntent getGatewaySafely(String s) {
        try {
            return GatewayIntent.valueOf(s);
        } catch (IllegalArgumentException ex) {
            return GatewayIntent.DIRECT_MESSAGES;
        }
    }
}
