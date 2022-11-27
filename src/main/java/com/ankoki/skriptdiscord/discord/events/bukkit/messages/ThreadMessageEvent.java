package com.ankoki.skriptdiscord.discord.events.bukkit.messages;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;

public class ThreadMessageEvent extends GuildMessageEvent {

	/**
	 * Creates a new thread message event.
	 *
	 * @param bot     the bot to use in this event.
	 * @param member  the member who said the message.
	 * @param message the message that was said.
	 * @param channel the thread channel it was said in.
	 */
	public ThreadMessageEvent(DiscordBot bot, Member member, Message message, ThreadChannel channel) {
		super(bot, member, message, channel);
	}

	@Override
	public ThreadChannel getChannel() {
		return (ThreadChannel) super.getChannel();
	}

}
