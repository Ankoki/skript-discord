package com.ankoki.skriptdiscord.discord.events.bukkit.messages.guild;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.discord.events.bukkit.messages.MessageEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

public class GuildMessageEvent extends MessageEvent {

	private final Member member;
	private final Guild guild;

	/**
	 * Creates a new bot event.
	 *
	 * @param bot the bot to use in this event.
	 */
	public GuildMessageEvent(DiscordBot bot, Member member, Message message, GuildMessageChannel channel) {
		super(bot, member.getUser(), message, channel);
		this.member = member;
		this.guild = member.getGuild();
	}

	/**
	 * Gets the member that sent this message.
	 *
	 * @return the member object.
	 */
	public Member getMember() {
		return member;
	}

	@Override
	public GuildMessageChannel getChannel() {
		return (GuildMessageChannel) super.getChannel();
	}

	/**
	 * Gets the guild this message was sent in.
	 *
	 * @return the guild.
	 */
	public Guild getGuild() {
		return guild;
	}

}
