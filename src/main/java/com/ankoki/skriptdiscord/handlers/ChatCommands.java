package com.ankoki.skriptdiscord.handlers;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.Trigger;
import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.elements.expressions.ExprArgument;
import com.ankoki.skriptdiscord.misc.Misc;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatCommands {

	private final DiscordBot bot;
	private final Map<String, Command> commands = new ConcurrentHashMap<>();

	/**
	 * Creates a new command handler with an owning bot.
	 *
	 * @param bot the bot to own this handler.
	 */
	public ChatCommands(DiscordBot bot) {
		this.bot = bot;
	}

	/**
	 * Gets the owning bot.
	 *
	 * @return the bot.
	 */
	public DiscordBot getBot() {
		return bot;
	}

	/**
	 * Registers a command with the given name and arguments.
	 *
	 * @param trigger   the trigger to run when executed.
	 * @param command   the command name to register.
	 * @param arguments the arguments to register. Only the last argument may be optional.
	 * @return true if successful, or false if the command name is taken.
	 */
	public boolean registerCommand(Trigger trigger, String command, Argument... arguments) {
		if (commands.containsKey(command))
			return false;
		commands.put(command, new Command(this.getBot(), new Arguments(arguments), trigger));
		return true;
	}

	/**
	 * Unregisters a discord bot command.
	 *
	 * @param name the name of the command to unregister.
	 * @return true if successful, false if the bot didn't have that command.
	 */
	public boolean unregisterCommand(String name) {
		return commands.remove(name) != null;
	}

	/**
	 * Executes a certain command with the given arguments.
	 *
	 * @param event     the event to use.
	 * @param cmd       the command to execute.
	 * @param arguments the arguments to execute with.
	 * @return true if executed successfully.
	 */
	public boolean execute(Event event, String cmd, String... arguments) {
		if (!commands.containsKey(cmd))
			return false;
		Command command = commands.get(cmd);
		return command.execute(event, arguments);
	}

	/**
	 * Gets a command by its name. If this command doesn't exist, return null.
	 * See {@link ChatCommands#hasCommand(String)}
	 *
	 * @param name the name to look for.
	 * @return the command if it exists.
	 */
	@Nullable
	public Command getCommand(String name) {
		return commands.get(name);
	}

	/**
	 * Checks if a command exists/is registered from a raw message.
	 * Can be a full message or just the command name.
	 *
	 * @param string the message.
	 * @return true if the command exists.
	 */
	public boolean hasCommand(String string) {
		if (Misc.isNullOrEmpty(string))
			return false;
		String prefix = this.getBot().getPrefix();
		String name = string.split(" ")[0].replaceFirst(prefix, "");
		return commands.containsKey(name);
	}

	/**
	 * Holds a command and it's trigger.
	 */
	public static class Command {

		private final Arguments arguments;
		private final Trigger trigger;
		private final DiscordBot bot;

		/**
		 * Creates a new command with the given arguments.
		 *
		 * @param bot       the owning bot.
		 * @param arguments the arguments to use.
		 * @param trigger   the trigger to use.
		 */
		public Command(DiscordBot bot, Arguments arguments, Trigger trigger) {
			this.bot = bot;
			this.arguments = arguments;
			this.trigger = trigger;
		}

		/**
		 * Gets the owning bot.
		 *
		 * @return the owning bot.
		 */
		public DiscordBot getBot() {
			return bot;
		}

		/**
		 * Executes the trigger with the given arguments and event.
		 *
		 * @param event     the event to use.
		 * @param arguments the unparsed arguments to use.
		 * @return true if executed successfully.
		 */
		public boolean execute(Event event, String... arguments) {
			ExprArgument.registerEventValues(event, this.arguments.parse(arguments));
			trigger.execute(event);
			return true;
		}

	}

	/**
	 * Utility class which holds a single argument.
	 */
	public static class Argument {

		private final ClassInfo<?> classInfo;

		/**
		 * Creates an argument.
		 * Make sure to call {@link Arguments#validateClassInfo(ClassInfo)} before adding to an argument.
		 *
		 * @param classInfo the ClassInfo for this argument.
		 */
		public Argument(ClassInfo<?> classInfo) {
			this.classInfo = classInfo;
		}

		/**
		 * Parses the argument into an object.
		 *
		 * @param string the string to parse.
		 * @return the parsed object.
		 */
		public Object parse(String string) {
			return this.classInfo.getParser().parse(string, ParseContext.COMMAND);
		}

		/**
		 * Checks if this argument is a string. Used to check if the last is a string to get all excess objects.
		 *
		 * @return true if the info is a string.
		 */
		public boolean isString() {
			return this.classInfo.getC() == String.class;
		}

	}

	/**
	 * Class which assigns and parses arguments based on {@link ClassInfo}, given in the same order.
	 */
	public static class Arguments {

		/**
		 * Checks if a ClassInfo can be used as an argument.
		 *
		 * @param classInfo the ClassInfo to check.
		 * @return true if it can be used, else false.
		 */
		public static boolean validateClassInfo(ClassInfo<?> classInfo) {
			if (classInfo == null || classInfo.getParser() == null)
				return false;
			try {
				classInfo.getParser().parse(".", ParseContext.COMMAND);
				return true;
			} catch (UnsupportedOperationException ex) {
				return false;
			}
		}

		private final Argument[] arguments;

		/**
		 * Creates a new argument chain for a command.
		 *
		 * @param arguments the arguments for this command.
		 */
		public Arguments(Argument... arguments) {
			this.arguments = arguments;
		}

		/**
		 * Checks if there are arguments.
		 *
		 * @return true if arguments are present.
		 */
		public boolean hasArguments() {
			return arguments.length > 0;
		}

		/**
		 * Parses the given strings as the given arguments.
		 * If the last class type is a string, all the remaining unparsed strings will collect as one.
		 *
		 * @param unparsed the unparsed arguments. Size must be the same or more than the current arguments.
		 * @return the parsed objects.
		 */
		public Object[] parse(String... unparsed) {
			if (!this.hasArguments())
				return new Object[0];
			Object[] parsed = new Object[arguments.length];
			boolean stringLast = arguments[arguments.length - 1].isString();
			for (int i = 0; i < unparsed.length; i++)
				parsed[i] = arguments[i].parse(unparsed[i]);
			if (stringLast && unparsed.length > parsed.length)
				parsed[arguments.length - 1] = String.join(" ", Arrays.copyOfRange(unparsed, arguments.length - 1, parsed.length));
			return parsed;
		}

	}

}
