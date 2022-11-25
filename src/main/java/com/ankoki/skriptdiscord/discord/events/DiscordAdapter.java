package com.ankoki.skriptdiscord.discord.events;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordAdapter extends ListenerAdapter {

	private final DiscordBot bot;

	/**
	 * Creates a new discord event adapter which listens to events.
	 *
	 * @param bot the bot which will listen to these events.
	 */
	public DiscordAdapter(DiscordBot bot) {
		this.bot = bot;
	}

	/**
	 * Gets the bot associated with these events.
	 *
	 * @return the bot.
	 */
	public DiscordBot getBot() {
		return bot;
	}
}
