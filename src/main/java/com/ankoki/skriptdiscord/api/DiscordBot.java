package com.ankoki.skriptdiscord.api;

import com.ankoki.skriptdiscord.SkriptDiscord;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import com.ankoki.skriptdiscord.api.DiscordMessage.MessageType;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;

public class DiscordBot {

    private final JDA jda;
    private final String name;
    private final String description;

    public DiscordBot(BotBuilder builder) {
        this(builder.getName(), builder.getDescription(), builder.getJda());
    }

    public DiscordBot(String name, JDA jda) {
        this(name, "<none>", jda);
    }

    public DiscordBot(String name, String description, JDA jda) {
        this.name = name;
        this.description = description;
        this.jda = jda;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public JDA getJda() {
        return jda;
    }

    public void sendMessage(MessageChannel channel, DiscordMessage message) {
        Bukkit.getScheduler().runTaskAsynchronously(SkriptDiscord.getInstance(), () -> {
            if (message.getType() == MessageType.TEXT) channel.sendMessage(message.getMessage()).queue();
            else channel.sendMessageEmbeds(message.getEmbed()).queue();
        });
    }

    public void sendMessage(User user, DiscordMessage message) {
        System.out.println("sent");
        Bukkit.getScheduler().runTaskAsynchronously(SkriptDiscord.getInstance(), () -> {
            user.openPrivateChannel().queue(channel -> {
                if (message.getType() == MessageType.TEXT) channel.sendMessage(message.getMessage()).queue();
                else channel.sendMessageEmbeds(message.getEmbed()).queue();
            });
        });
    }

    public void sendMessage(Member member, DiscordMessage message) {
        sendMessage(member.getUser(), message);
    }
}
