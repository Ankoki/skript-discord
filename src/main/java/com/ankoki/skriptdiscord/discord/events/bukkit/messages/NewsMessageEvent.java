package com.ankoki.skriptdiscord.discord.events.bukkit.messages;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;

public class NewsMessageEvent extends GuildMessageEvent {

	/**
	 * Creates a news message event.
	 *
	 * @param bot     the bot to use in this event.
	 * @param member  the member who sent the message.
	 * @param message the sent message.
	 * @param channel the news channel it was sent in.
	 */
	public NewsMessageEvent(DiscordBot bot, Member member, Message message, NewsChannel channel) {
		super(bot, member, message, channel);
	}

	@Override
	public NewsChannel getChannel() {
		return (NewsChannel) super.getChannel();
	}

}
