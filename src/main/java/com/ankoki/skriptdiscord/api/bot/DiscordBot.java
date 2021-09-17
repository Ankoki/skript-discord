package com.ankoki.skriptdiscord.api.bot;

import com.ankoki.skriptdiscord.api.DiscordMessage;
import com.ankoki.skriptdiscord.utils.Console;
import com.ankoki.skriptdiscord.utils.Utils;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import com.ankoki.skriptdiscord.api.DiscordMessage.MessageType;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

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

    public boolean hasPermission(Guild guild, Permission... permissions) {
        return guild.getSelfMember().hasPermission(permissions);
    }

    public boolean hasPermission(Guild guild, TextChannel channel, Permission... permissions) {
        return guild.getSelfMember().hasPermission(channel, permissions);
    }

    public void sendMessage(MessageChannel channel, DiscordMessage message) {
        if (!this.hasIntent(GatewayIntent.GUILD_MESSAGES)) {
            Console.error("You do not have the correct intent (guild messages) enabled for this bot (" + name + ")");
        } else if (channel.getType().isGuild() && !(((TextChannel) channel).getGuild().getSelfMember().hasPermission((TextChannel) channel, Permission.MESSAGE_WRITE))) {
            Console.error("You do not have permission to type in this channel (" + channel.getName() + ", " + name + ")");
        } else {
            Utils.runAsync(() -> {
                if (message.getType() == MessageType.TEXT) channel.sendMessage(message.getMessage()).queue();
                else channel.sendMessageEmbeds(message.getEmbed()).queue();
            });
        }
    }

    public void sendMessage(User user, DiscordMessage message) {
        if (!this.hasIntent(GatewayIntent.DIRECT_MESSAGES)) {
            Console.error("You do not have the correct intent (direct messages) enabled for this bot (" + name + ")");
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
        if (!this.hasPermission(guild, Permission.NICKNAME_CHANGE)) {
            Console.error("You do not have the correct permission (nickname change) in this guild (" +
                    guild.getName() + ", " + name + ")");
        } else if (!guild.getSelfMember().canInteract(member)) {
            Console.error("You cannot interact with this member (" + member.getEffectiveName() + ")");
        } else {
            Utils.runAsync(() -> member.modifyNickname(nickname).queue());
        }
    }

    public void replyTo(Message message, DiscordMessage with) {
        if (!this.hasIntent(GatewayIntent.GUILD_MESSAGES)) {
            Console.error("You do not have the correct intent (guild messages) enabled for this bot (" + name + ")");
        } else if (!message.getGuild().getSelfMember().hasPermission(message.getTextChannel(), Permission.MESSAGE_WRITE)) {
            Console.error("You do not have permission to type in this channel (" +
                    message.getTextChannel().getName() + ", " + name + ")");
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
                Console.error("You cannot edit messages from other people. (" + name + ")");
            }
            else if (with.getType() == MessageType.EMBED) message.editMessageEmbeds(with.getEmbed()).queue();
            else message.editMessage(with.getMessage()).queue();
        });
    }

    public Message getMessage(MessageChannel channel, String id) {
        if (!this.hasIntent(GatewayIntent.GUILD_MESSAGES)) {
            Console.error("You do not have the correct intent (guild messages) enabled for this bot (" + name + ")");
        }
        else if (channel instanceof TextChannel) {
            if (!this.hasPermission(((TextChannel) channel).getGuild(),
                    (TextChannel) channel,
                    Permission.MESSAGE_HISTORY,
                    Permission.MESSAGE_READ)) {
                Console.error("You do not have permission to read messages or get message history in this channel (" +
                        channel.getName() + ", " + name + ")");
            }
        }
        CompletableFuture<Message> future = CompletableFuture.supplyAsync(() -> channel.retrieveMessageById(id).complete());
        while (!future.isDone()) {}
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException ex) {
            Utils.throwException(ex);
        }
        return null;
    }

    public Guild getGuild(String id) {
        CompletableFuture<Guild> future = CompletableFuture.supplyAsync(() -> getJda().getGuildById(id));
        while (!future.isDone()) {}
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException ex) {
            Utils.throwException(ex);
        }
        return null;
    }

    public TextChannel getChannel(Guild guild, String id) {
        CompletableFuture<TextChannel> future = CompletableFuture.supplyAsync(() -> guild.getTextChannelById(String.valueOf(id)));
        while (!future.isDone()) {}
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException ex) {
            Utils.throwException(ex);
        }
        return null;
    }

    public User getUser(Guild guild, String id) {
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            AtomicReference<User> user = new AtomicReference<>();
            guild.retrieveMemberById(id).queue(member -> user.set(member.getUser()));
            while (user.get() == null) {}
            return user.get();
        });
        while (!future.isDone()) {}
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException ex) {
            Utils.throwException(ex);
        }
        return null;
    }

    public Member getMember(Guild guild, String id) {
        CompletableFuture<Member> future = CompletableFuture.supplyAsync(() -> {
            AtomicReference<Member> user = new AtomicReference<>();
            guild.retrieveMemberById(id).queue(user::set);
            while (user.get() == null) {}
            return user.get();
        });
        while (!future.isDone()) {}
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException ex) {
            Utils.throwException(ex);
        }
        return null;
    }

    @Override
    public String toString() {
        return "{\"name\": \"" + name + ", \"description\": " + description + "}";
    }
}
