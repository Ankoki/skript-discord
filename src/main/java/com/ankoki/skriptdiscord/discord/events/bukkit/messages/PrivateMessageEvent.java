package com.ankoki.skriptdiscord.discord.events.bukkit.messages;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;

public class PrivateMessageEvent extends MessageEvent {

	/**
	 * Creates a new private message event.
	 *
	 * @param bot     the bot to use in this event.
	 * @param user    the user who sent the message.
	 * @param message the message that was sent.
	 * @param channel the channel the message was sent in.
	 */
	public PrivateMessageEvent(DiscordBot bot, User user, Message message, PrivateChannel channel) {
		super(bot, user, message, channel);
	}

	@Override
	public PrivateChannel getChannel() {
		return (PrivateChannel) super.getChannel();
	}

}
