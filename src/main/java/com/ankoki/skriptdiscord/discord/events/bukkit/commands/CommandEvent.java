package com.ankoki.skriptdiscord.discord.events.bukkit.commands;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.discord.events.bukkit.BotEvent;
import org.bukkit.event.Cancellable;

public class CommandEvent extends BotEvent implements Cancellable {

	private final String[] arguments;
	private final boolean slash;
	private boolean cancelled;

	/**
	 * Creates a new command event.
	 *
	 * @param bot       the bot to use in this event.
	 * @param slash     if this event is a slash command.
	 * @param arguments the arguments of this event.
	 */
	public CommandEvent(DiscordBot bot, boolean slash, String... arguments) {
		super(bot);
		this.slash = slash;
		this.arguments = arguments;
	}

	/**
	 * Gets the arguments associated with this event.
	 *
	 * @return the arguments.
	 */
	public String[] getArguments() {
		return arguments;
	}

	/**
	 * Checks if this command is a slash event.
	 * If this method returns true, this can be safely cast to a {@link SlashCommandEvent},
	 * or if false, it can be cast to a {@link MessageCommandEvent}.
	 *
	 * @return true if slash event, else false.
	 */
	public boolean isSlash() {
		return slash;
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
