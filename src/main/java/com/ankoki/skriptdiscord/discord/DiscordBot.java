package com.ankoki.skriptdiscord.discord;

import com.ankoki.skriptdiscord.handlers.CommandHandler;
import com.ankoki.skriptdiscord.handlers.DataHandler;
import com.ankoki.skriptdiscord.misc.Misc;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;

public class DiscordBot {

	// Constants
	private final String name,
			prefix;
	private final Permission[] permissions;
	private final CommandHandler commandHandler = new CommandHandler(this);
	private final boolean setup;

	// Local Fields
	private JDA jda;
	private boolean ready = false;

	/**
	 * Creates a new Discord bot and registers it.
	 *
	 * @param name        the name to use for the bot. Must be unique.
	 * @param prefix      the prefix this bot responds to. Must be one character.
	 * @param token       the token to log into this bot with.
	 * @param permissions the permissions this bot should have.
	 */
	public DiscordBot(String name, String prefix, String token, Permission... permissions) {
		this.name = name;
		this.prefix = prefix;
		this.permissions = permissions;
		this.setup = DataHandler.registerBot(this, token);
	}

	/**
	 * Gets the name of the bot.
	 *
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the prefix of this bot.
	 *
	 * @return the prefix.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Checks if the bot has been setup.
	 *
	 * @return true if setup.
	 */
	public boolean isSetup() {
		return setup;
	}

	/**
	 * Checks if this bot has a certain permission.
	 *
	 * @param permission the permission to look for.
	 * @return true if contains.
	 */
	public boolean hasPermission(Permission permission) {
		return Misc.arrayContains(this.permissions, permission);
	}

	/**
	 * Checks if this bot is ready, differs to {@link DiscordBot#isSetup()}, as setup ensures it is registered.
	 * However, you should check both if you are trying to do anything with this bot.
	 *
	 * @return true if ready.
	 */
	public boolean isReady() {
		return this.ready;
	}

	/**
	 * Sets if this bot is ready. Should only be used internally in {@link DataHandler}.
	 *
	 * @param ready the new ready status.
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	/**
	 * Gets the JDA instance of this bot.
	 *
	 * @return the JDA instance.
	 */
	public JDA getJDA() {
		return this.jda;
	}

	/**
	 * Sets the JDA instance of this bot.
	 *
	 * @param jda the new JDA instance.
	 */
	public void setJDA(JDA jda) {
		this.jda = jda;
	}

	/**
	 * Gets the command handler associated with this bot.
	 *
	 * @return the current command handler.
	 */
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	/**
	 * Disables and unregisters this bot.
	 */
	public void disable() {
		this.disable(false);
	}

	/**
	 * Disables and unregisters this bot.
	 *
	 * @param force if true, shutdown will occur and not execute any of the pending
	 * {@link net.dv8tion.jda.api.requests.RestAction}'s.
	 */
	public void disable(boolean force) {
		if (force)
			this.jda.shutdownNow();
		else
			this.jda.shutdown();
		DataHandler.unregisterBot(this);
	}

}
