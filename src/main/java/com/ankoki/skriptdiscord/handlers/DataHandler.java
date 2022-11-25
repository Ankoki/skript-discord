package com.ankoki.skriptdiscord.handlers;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.discord.events.DiscordAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DataHandler {

	private static final Map<String, DiscordBot> DISCORD_BOTS = new ConcurrentHashMap<>();

	/**
	 * Registers a discord bot. This will also log into it.
	 *
	 * @param token the token to log into the bot with.
	 * @return true if successful, false if another bot already has this name.
	 */
	public static boolean registerBot(DiscordBot bot, String token) {
		if (DISCORD_BOTS.containsKey(bot.getName()))
			return false;
		else {
			JDA jda = JDABuilder.createDefault(token).build();
			jda.addEventListener(new DiscordAdapter(bot));
			CompletableFuture.runAsync(() -> {
				try {
					jda.awaitReady();
					bot.setReady(true);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			});
			bot.setJDA(jda);
			DISCORD_BOTS.put(bot.getName(), bot);
			return true;
		}
	}

	/**
	 * Gets a discord bot by name. Might be null.
	 *
	 * @param name the name of the bot.
	 * @return the discord bot with this name, or null.
	 */
	@Nullable
	public static DiscordBot getBot(String name) {
		return DISCORD_BOTS.get(name);
	}

	/**
	 * Gets a discord bot by its JDA instance. Might be null.
	 *
	 * @param jda the jda instance to search for.
	 * @return the discord bot with this JDA instance, or null.
	 */
	@Nullable
	public static DiscordBot getBot(JDA jda) {
		for (DiscordBot bot : DISCORD_BOTS.values())
			if (bot.getJDA() == jda)
				return bot;
		return null;
	}

	/**
	 * Gets all registered discord bots.
	 *
	 * @return all the registered bots.
	 */
	public static DiscordBot[] getBots() {
		return DISCORD_BOTS.values().toArray(new DiscordBot[0]);
	}

	/**
	 * Unregisters a bot.
	 *
	 * @param bot the bot to unregister.
	 */
	public static void unregisterBot(DiscordBot bot) {
		DISCORD_BOTS.remove(bot.getName());
	}

}
