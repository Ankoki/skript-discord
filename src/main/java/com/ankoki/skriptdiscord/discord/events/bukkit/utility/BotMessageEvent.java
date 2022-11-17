package com.ankoki.skriptdiscord.discord.events.bukkit.utility;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.bukkit.event.Cancellable;

public class BotMessageEvent extends BotEvent implements Cancellable {

	private boolean cancelled;
	private Member member;
	private Message message;
	private Guild guild;
	private Channel channel;

	/**
	 * Creates a new bot event.
	 *
	 * @param bot the bot to use in this event.
	 */
	public BotMessageEvent(DiscordBot bot, Member member, Message message, Guild guild, Channel channel) {
		super(bot);
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
