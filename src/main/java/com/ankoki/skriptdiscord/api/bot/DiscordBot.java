package com.ankoki.skriptdiscord.api.bot;

import ch.njol.skript.Skript;
import com.ankoki.skriptdiscord.SkriptDiscord;
import com.ankoki.skriptdiscord.api.DiscordMessage;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import com.ankoki.skriptdiscord.api.DiscordMessage.MessageType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;

public class DiscordBot {

    @Getter
    private final JDA jda;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final String prefix;

    public DiscordBot(String name, String description, JDA jda, String prefix) {
        this.name = name;
        this.description = description;
        this.jda = jda;
        this.prefix = prefix;
    }

    public DiscordBot(String name, String description, JDA jda) {
        this(name, description, jda, null);
    }

    public DiscordBot(BotBuilder builder) {
        this(builder.getName(), builder.getDescription(), builder.getJda(), null);
    }

    public DiscordBot(String name, JDA jda) {
        this(name, "<none>", jda, null);
    }

    public boolean hasIntent(GatewayIntent intent) {
        return jda.getGatewayIntents().contains(intent);
    }

    public void sendMessage(MessageChannel channel, DiscordMessage message) {
        if (!this.hasIntent(GatewayIntent.GUILD_MESSAGES)) {
            Skript.error("You do not have the correct intent (guild messages) enabled for this bot (" + name + ")");
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(SkriptDiscord.getInstance(), () -> {
                if (message.getType() == MessageType.TEXT) channel.sendMessage(message.getMessage()).queue();
                else channel.sendMessageEmbeds(message.getEmbed()).queue();
            });
        }
    }

    public void sendMessage(User user, DiscordMessage message) {
        if (!this.hasIntent(GatewayIntent.DIRECT_MESSAGES)) {
            Skript.error("You do not have the correct intent (direct messages) enabled for this bot (" + name + ")");
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(SkriptDiscord.getInstance(), () ->
                user.openPrivateChannel().queue(channel -> {
                    if (message.getType() == MessageType.TEXT) channel.sendMessage(message.getMessage()).queue();
                    else channel.sendMessageEmbeds(message.getEmbed()).queue();
            }));
        }
    }

    public void sendMessage(Member member, DiscordMessage message) {
        sendMessage(member.getUser(), message);
    }

    @Override
    public String toString() {
        return "{\"name\": \"" + name + ", \"description\": " + description + "}";
    }
}
