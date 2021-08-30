package com.ankoki.skriptdiscord.api.bot;

import ch.njol.skript.Skript;
import com.ankoki.skriptdiscord.api.DiscordMessage;
import com.ankoki.skriptdiscord.utils.Utils;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import com.ankoki.skriptdiscord.api.DiscordMessage.MessageType;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot {

    @Getter
    private final JDA jda;
    @Getter
    private final String name;
    @Getter
    private final String description;

    public DiscordBot(String name, String description, JDA jda) {
        this.name = name;
        this.description = description;
        this.jda = jda;
    }

    public DiscordBot(BotBuilder builder) {
        this(builder.getName(), builder.getDescription(), builder.getJda());
    }

    public DiscordBot(String name, JDA jda) {
        this(name, null, jda);
    }

    public boolean hasIntent(GatewayIntent intent) {
        return jda.getGatewayIntents().contains(intent);
    }

    public void sendMessage(MessageChannel channel, DiscordMessage message) {
        if (!this.hasIntent(GatewayIntent.GUILD_MESSAGES)) {
            Skript.error("You do not have the correct intent (guild messages) enabled for this bot (" + name + ")");
        } else if (channel.getType().isGuild() && !(((TextChannel) channel).getGuild().getSelfMember().hasPermission((TextChannel) channel, Permission.MESSAGE_WRITE))) {
            Skript.error("You do not have permission to type in this channel (#" + channel.getName() + ")");
        } else {
            Utils.runAsync(() -> {
                if (message.getType() == MessageType.TEXT) channel.sendMessage(message.getMessage()).queue();
                else channel.sendMessageEmbeds(message.getEmbed()).queue();
            });
        }
    }

    public void sendMessage(User user, DiscordMessage message) {
        if (!this.hasIntent(GatewayIntent.DIRECT_MESSAGES)) {
            Skript.error("You do not have the correct intent (direct messages) enabled for this bot (" + name + ")");
        } else {
            Utils.runAsync(() ->
                    user.openPrivateChannel().queue(channel -> {
                        if (message.getType() == MessageType.TEXT) channel.sendMessage(message.getMessage()).queue();
                        else channel.sendMessageEmbeds(message.getEmbed()).queue();
                    }));
        }
    }

    public void sendMessage(Member member, DiscordMessage message) {
        sendMessage(member.getUser(), message);
    }

    public void changeNickname(Member member, String nickname) {
        if (member == null) return;
        Guild guild = member.getGuild();
        if (!guild.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) {
            Skript.error("You do not have the correct permission (nickname change) in this guild (" + guild.getName() + ")");
        } else if (!guild.getSelfMember().canInteract(member)) {
            Skript.error("You cannot interact with this member (" + member.getEffectiveName() + ")");
        } else {
            Utils.runAsync(() -> {
                member.modifyNickname(nickname).queue();
            });
        }
    }

    public void replyTo(Message message, DiscordMessage with) {
        if (!this.hasIntent(GatewayIntent.GUILD_MESSAGES)) {
            Skript.error("You do not have the correct intent (guild messages) enabled for this bot (" + name + ")");
        } else if (!message.getGuild().getSelfMember().hasPermission(message.getTextChannel(), Permission.MESSAGE_WRITE)) {
            Skript.error("You do not have permission to type in this channel (" + message.getTextChannel().getName() + ")");
        } else {
            Utils.runAsync(() -> {
                if (with.getType() == MessageType.EMBED) message.replyEmbeds(with.getEmbed()).queue();
                else message.reply(with.getMessage()).queue();
            });
        }
    }

    public void edit(Message message, DiscordMessage with) {
        Utils.runAsync(() -> {
            if (message.getAuthor() != message.getGuild().getSelfMember().getUser()) {
                Skript.error("You cannot edit messages from other people");
                return;
            }
            if (with.getType() == MessageType.EMBED) message.editMessageEmbeds(with.getEmbed()).queue();
            else message.editMessage(with.getMessage()).queue();
        });
    }

    @Override
    public String toString() {
        return "{\"name\": \"" + name + ", \"description\": " + description + "}";
    }
}
