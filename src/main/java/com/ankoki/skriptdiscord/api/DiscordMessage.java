package com.ankoki.skriptdiscord.api;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class DiscordMessage {

    private String message;
    private MessageEmbed embed;
    private final MessageType type;

    public DiscordMessage(String message) {
        this.message = message;
        type = MessageType.TEXT;
    }

    public DiscordMessage(MessageEmbed embed) {
        this.embed = embed;
        type = MessageType.EMBED;
        message = embed.getTitle() + ":" + embed.getDescription();
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
