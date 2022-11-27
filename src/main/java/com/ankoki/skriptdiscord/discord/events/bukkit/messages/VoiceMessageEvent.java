package com.ankoki.skriptdiscord.discord.events.bukkit.messages;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class VoiceMessageEvent extends GuildMessageEvent {

	/**
	 * Creates a voice message event.
	 *
	 * @param bot     the bot to use in this event.
	 * @param member  the member who send the message.
	 * @param message the sent message.
	 * @param channel the channel it was sent in.
	 */
	public VoiceMessageEvent(DiscordBot bot, Member member, Message message, VoiceChannel channel) {
		super(bot, member, message, channel);
	}

	@Override
	public VoiceChannel getChannel() {
		return (VoiceChannel) super.getChannel();
	}
}
