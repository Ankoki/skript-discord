package com.ankoki.skriptdiscord.elements.structures;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.ContextlessEvent;
import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.misc.Misc;
import net.dv8tion.jda.api.Permission;
import org.bukkit.event.Event;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.lang.structure.Structure;

@Name("Bot Creation")
@Description("Registers a new discord bot. Must contain a unique name, prefix (with only one character), and a discord application token.")
@Examples("""
				create discord bot:
					name: ByeolBot
					prefix: ;
					token: xlkawjnfbuahwf910198r
		""")
@Since("1.0")
public class StructBot extends Structure {

	static {
		Skript.registerStructure(StructBot.class,
				EntryValidator.builder()
						.addEntry("name", null, false)
						.addEntry("prefix", null, false)
						.addEntry("token", null, false)
						.addEntryData(new ExpressionEntryData<>("permissions", null, true, String.class, ContextlessEvent.class))
						.build(),
				"register [a] [new] [discord] bot");
	}

	private DiscordBot currentBot;

	@Override
	public boolean init(Literal<?>[] literals, int i, ParseResult parseResult, EntryContainer entryContainer) {
		return true;
	}

	@Override
	public boolean load() {
		EntryContainer container = this.getEntryContainer();
		String name = container.get("name", String.class, false);
		String prefix = container.get("prefix", String.class, false);
		String token = container.get("token", String.class, false);
		Expression<?> permissionsExpr = container.getOptional("permissions", Expression.class, false);
		String[] permissions = new String[0];
		if (permissionsExpr != null)
			permissions = (String[]) permissionsExpr.getAll(ContextlessEvent.get());
		this.currentBot = new DiscordBot(name, prefix, token, Misc.mapToEnum(Permission.class, permissions));
		return this.currentBot.isSetup();
	}

	@Override
	public void unload() {
		this.currentBot.disable();
	}

	@Override
	public String toString(Event event, boolean b) {
		return "register discord bot";
	}

}
