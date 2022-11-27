package com.ankoki.skriptdiscord.discord.events.bukkit.commands;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class MessageCommandEvent extends CommandEvent {

	private final User user;
	private final MessageChannel channel;

	/**
	 * Creates a new command event.
	 *
	 * @param bot     the bot to use in this event.
	 * @param user    the member who used this command.
	 * @param channel the channel this event was used in.
	 */
	public MessageCommandEvent(DiscordBot bot, User user, MessageChannel channel, String... arguments) {
		super(bot, false, arguments);
		this.user = user;
		this.channel = channel;
	}

	/**
	 * Gets the member who used this command.
	 *
	 * @return the member.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Gets the channel this command was used in.
	 *
	 * @return the channel.
	 */
	public MessageChannel getChannel() {
		return channel;
	}

}
