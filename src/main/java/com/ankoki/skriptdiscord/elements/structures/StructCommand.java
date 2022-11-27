package com.ankoki.skriptdiscord.elements.structures;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.util.ContextlessEvent;
import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.discord.events.bukkit.commands.CommandEvent;
import com.ankoki.skriptdiscord.handlers.ChatCommands;
import com.ankoki.skriptdiscord.handlers.DataHandler;
import com.ankoki.skriptdiscord.misc.Misc;
import org.bukkit.event.Event;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.lang.entry.util.TriggerEntryData;
import org.skriptlang.skript.lang.structure.Structure;

@Name("Discord Command")
@Description("Creates a new discord slash or message command.")
@Examples("""
		discord command "echo":
				bots: ByeolBot
				arguments: strings
				trigger:
					reply to event-message with ""
		""")
@Since("1.0")
public class StructCommand extends Structure {

	static {
		Skript.registerStructure(StructCommand.class,
				EntryValidator.builder()
						.addEntryData(new ExpressionEntryData<>("bots", null, false, String.class, ContextlessEvent.class))
						.addEntryData(new ExpressionEntryData<>("arguments", null, true, ClassInfo.class, ContextlessEvent.class))
						.addEntryData(new TriggerEntryData("trigger", null, false, CommandEvent.class))
						.build(),
				"discord [:slash] command %string%");
	}

	private String name;
	private DiscordBot[] bots;
	private boolean slash, allowed;

	@Override
	public boolean init(Literal<?>[] literals, int i, ParseResult parseResult, EntryContainer entryContainer) {
		name = (String) literals[0].getSingle();
		slash = parseResult.hasTag("slash");
		if (Misc.isNullOrEmpty(name))
			return false;
		else if (name.contains(" ")) {
			Skript.error("You cannot use spaces in a command name.");
			return false;
		}
		allowed = true;
		return true;
	}

	@Override
	public boolean load() {
		EntryContainer container = this.getEntryContainer();
		ClassInfo[] args = (ClassInfo[]) container.get("arguments", Literal.class, false).getAll(ContextlessEvent.get());
		if (!Misc.checkArguments(args))
			return false;
		ChatCommands.Argument[] arguments = new ChatCommands.Argument[args.length];
		for (int i = 0; i < args.length; i++)
			arguments[i] = new ChatCommands.Argument(args[i]);
		String[] bots = (String[]) container.get("bots", Expression.class, false).getAll(ContextlessEvent.get());
		this.bots = new DiscordBot[bots.length];
		Trigger trigger = container.get("trigger", Trigger.class, false);
		boolean botAlive = false;
		int i = 0;
		for (String name : bots) {
			DiscordBot bot = DataHandler.getBot(name);
			if (bot != null) {
				this.bots[i] = bot;
				i++;
				if (slash) {
					botAlive = true;
					// TODO Slash Commands.
				} else {
					if (bot.getCommandHandler().registerCommand(trigger, name, arguments))
						botAlive = true;
				}
			}
		}
		return botAlive;
	}

	@Override
	public void unload() {
		if (allowed) {
			for (DiscordBot bot : this.bots)
				bot.getCommandHandler().unregisterCommand(name);
		}
		super.unload();
	}

	@Override
	public String toString(Event event, boolean b) {
		return "discord command " + name;
	}

}
