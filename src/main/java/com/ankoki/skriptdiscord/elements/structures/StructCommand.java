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
import ch.njol.skript.lang.util.ContextlessEvent;
import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.discord.events.bukkit.MessageCommandEvent;
import com.ankoki.skriptdiscord.handlers.DataHandler;
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
						.addEntryData(new TriggerEntryData("trigger", null, false, MessageCommandEvent.class)) // CommandEvent pls.
						.build(),
				"discord [:slash] command %string%");
	}

	private String name;
	private boolean slash;

	@Override
	public boolean init(Literal<?>[] literals, int i, ParseResult parseResult, EntryContainer entryContainer) {
		name = (String) literals[0].getSingle();
		slash = parseResult.hasTag("slash");
		return name != null;
	}

	@Override
	public boolean load() {
		EntryContainer container = this.getEntryContainer();
		String[] bots = (String[]) container.get("bots", Expression.class, false).getAll(ContextlessEvent.get());
		boolean botAlive = false;
		for (String name : bots) {
			DiscordBot bot = DataHandler.getBot(name);
			if (bot != null) {
				botAlive = true;

			}
		}
		return botAlive;
	}

	@Override
	public void unload() {
		super.unload();
	}

	@Override
	public String toString(Event event, boolean b) {
		return "discord command " + name;
	}

}
