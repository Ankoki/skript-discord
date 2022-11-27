package com.ankoki.skriptdiscord.discord.events.bukkit.messages;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.discord.events.bukkit.BotEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.bukkit.event.Cancellable;

public class MessageEvent extends BotEvent implements Cancellable {

	private boolean cancelled;
	private final User user;
	private final Message message;
	private final MessageChannel channel;

	/**
	 * Creates a new bot event.
	 *
	 * @param bot the bot to use in this event.
	 */
	public MessageEvent(DiscordBot bot, User user, Message message, MessageChannel channel) {
		super(bot);
		this.user = user;
		this.message = message;
		this.channel = channel;
	}

	/**
	 * Gets the user that sent this message.
	 *
	 * @return the user object.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Gets the message sent.
	 *
	 * @return the message.
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * Gets the channel this message was sent in.
	 *
	 * @return the channel this was sent in.
	 */
	public MessageChannel getChannel() {
		return channel;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

}
