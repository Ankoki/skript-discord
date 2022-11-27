package com.ankoki.skriptdiscord.discord.events.bukkit;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BotEvent extends Event {
	private static final HandlerList HANDLER_LIST = new HandlerList();

	private final DiscordBot bot;

	/**
	 * Creates a new bot event.
	 *
	 * @param bot the bot to use in this event.
	 */
	public BotEvent(DiscordBot bot) {
		this.bot = bot;
	}

	/**
	 * Gets the bot used in/that heard this event.
	 *
	 * @return the bot used in this event.
	 */
	public DiscordBot getBot() {
		return bot;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	/**
	 * REQUIRED BY BUKKIT.
	 *
	 * @return empty handler list.
	 */
	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

}
