package com.ankoki.skriptdiscord.api;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class DiscordMessage {

    private final String message;
    private final MessageEmbed embed;
    private final MessageType type;

    public DiscordMessage(String message) {
        this.message = message;
        this.type = MessageType.TEXT;
        embed = null;
    }

    public DiscordMessage(MessageEmbed embed) {
        this.embed = embed;
        this.type = MessageType.EMBED;
        this.message = "{\"" + embed.getTitle() + "\":\"" + embed.getDescription() + "\"}";
    }

    public DiscordMessage(Object object) {
        if (object instanceof String) {
            this.message = (String) object;
            this.type = MessageType.TEXT;
            this.embed = null;
        } else if (object instanceof MessageEmbed) {
            this.embed = (MessageEmbed) object;
            this.type = MessageType.EMBED;
            this.message = "{\"" + embed.getTitle() + "\":\"" + embed.getDescription() + "\"}";
        } else {
            this.message = String.valueOf(object);
            this.type = MessageType.TEXT;
            this.embed = null;
        }
    }

    public String getMessage() {
        return message;
    }

    public MessageEmbed getEmbed() {
        return embed;
    }

    public MessageType getType() {
        return type;
    }

    public enum MessageType {
        EMBED, TEXT;
    }
}
