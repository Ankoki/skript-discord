package com.ankoki.skriptdiscord;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.ankoki.skriptdiscord.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

// To access, use SkriptDiscord.getPlugin(SkriptDiscord.class)
public class SkriptDiscord extends JavaPlugin {

	private SkriptAddon addon;

	@Override
	public void onEnable() {
		if (!Misc.isEnabled("Skript")) {
			this.error("Skript was not found, is it on your server and enabled?");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		addon = Skript.registerAddon(this);
		this.registerElements();
		this.info("skript-discord has been enabled.");
	}

	/**
	 * Logs an informative message.
	 * @param message the message to log.
	 */
	public void info(String message) {
		this.getLogger().info(message);
	}

	/**
	 * Logs a debug message.
	 * @param message the message to log.
	 */
	public void debug(String message) {
		this.getLogger().warning("DEBUG | " + message);
	}

	/**
	 * Logs a warning message.
	 * @param message the message to log.
	 */
	public void warning(String message) {
		this.getLogger().warning(message);
	}

	/**
	 * Logs an error message.
	 * @param message the message to log.
	 */
	public void error(String message) {
		this.getLogger().severe(message);
	}

	private void registerElements() {
		if (Skript.isAcceptRegistrations()) {
			try {
				addon.loadClasses("com.ankoki.skriptdiscord.elements");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
