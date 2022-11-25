package com.ankoki.skriptdiscord.misc;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.handlers.DataHandler;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class Misc {

	/**
	 * Checks if plugins are enabled.
	 *
	 * @param plugins the name of the plugins.
	 * @return if all plugins are enabled.
	 */
	public static boolean isEnabled(String... plugins) {
		boolean enabled = true;
		for (String plugin : plugins) {
			Plugin pluginInst = Bukkit.getPluginManager().getPlugin(plugin);
			enabled = enabled && pluginInst != null && pluginInst.isEnabled();
		}
		return enabled;
	}

	/**
	 * Checks if an array contains a value.
	 *
	 * @param haystack the array to check.
	 * @param needle   the value to find.
	 * @param <Type>   the class type.
	 * @return true if the array contains the value.
	 */
	public static <Type> boolean arrayContains(Type[] haystack, Type needle) {
		for (Type value : haystack) {
			if (value == needle)
				return true;
		}
		return false;
	}

	/**
	 * Gets an enum value safely which returns null if not found. Whitespace and lowercase letters will be converted to
	 * uppercase and _.
	 *
	 * @param type the class of the enum.
	 * @param name the name of the value to look for.
	 * @param <E>  the class which extends enum to get the value for.
	 * @return the found value, or null.
	 */
	public static <E extends Enum<E>> E safeValueOf(Class<E> type, String name) {
		try {
			return Enum.valueOf(type, name.replace(" ", "_").toUpperCase());
		} catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}

	/**
	 * Maps a string list of enum names to the respective enums, not containing null.
	 *
	 * @param type   the enum class.
	 * @param values the string values to map.
	 * @param <E>    the class which extends enum to map the values to.
	 * @return the mapped values.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E[] mapToEnum(Class<E> type, String... values) {
		return (E[]) Arrays.stream(values)
				.map(value -> Misc.safeValueOf(type, value))
				.filter(Objects::nonNull)
				.toArray();
	}


	/**
	 * Checks if a text is safe to use for the given TextType. See {@link net.dv8tion.jda.api.entities.MessageEmbed.Field} for more information.
	 *
	 * @param text the text to check.
	 * @param type the type to check against.
	 * @return true if safe.
	 */
	public static boolean isDiscordSafe(String text, TextType type) {
		if (text == null)
			return false;
		return type.isSafe(text);
	}

	/**
	 * Utility enum to make sure strings are safe from exceptions.
	 */
	public enum TextType {
		TITLE(256),
		AUTHOR(256),
		VALUE(1024),
		DESCRIPTION(4096),
		FOOTER(2048),
		URL(2000),
		EMBED(6000);

		private final int maxLength;

		/**
		 * Defines the max length of a text for the given type.
		 *
		 * @param maxLength the max length.
		 */
		TextType(int maxLength) {
			this.maxLength = maxLength;
		}

		/**
		 * Checks if the given text is applicable for the type.
		 *
		 * @param text the text to check.
		 * @return true if it is.
		 */
		public boolean isSafe(String text) {
			return text.length() <= maxLength;
		}

	}

	/**
	 * Checks if the given message is a command.
	 *
	 * @param message the message to check.
	 * @return the bot that owns this command, or null.
	 */
	@Nullable
	public static DiscordBot isCommand(Message message) {
		for (DiscordBot bot : DataHandler.getBots()) {

		}
		return null;
	}

}
