package com.ankoki.skriptdiscord.discord.events.bukkit.commands;

import com.ankoki.skriptdiscord.discord.DiscordBot;

// TODO Slash Commands.
public class SlashCommandEvent extends CommandEvent {

	/**
	 * Creates a new slash command event.
	 *
	 * @param bot       the bot to use in this event.
	 * @param arguments the arguments of this event.
	 */
	public SlashCommandEvent(DiscordBot bot, Object... arguments) {
		super(bot, false, arguments);
	}

}
