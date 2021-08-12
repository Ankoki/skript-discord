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
}