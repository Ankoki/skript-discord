package com.ankoki.skriptdiscord.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.elements.structures.StructCommand;
import org.bukkit.event.Event;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.WeakHashMap;

@Name("Argument")
@Description("Gets an argument/all the arguments of a command.")
@Examples("""
		discord command "echo":
			bots: ByeolBot
			arguments: string
			trigger:
				reply to event-message with arg-1
		""")
@Since("1.0")
public class ExprArgument extends SimpleExpression<Object> {

	private static final WeakHashMap<Event, Object[]> EVENT_VALUES = new WeakHashMap<>();

	static {
		Skript.registerExpression(ExprArgument.class, Object.class, ExpressionType.SIMPLE,
				"[discord] [command] arg[ument]( |-)%number%",
				"all [of] [the] arg[ument]s");
	}

	/**
	 * Declares the values for an event.
	 * These will only be held while there is a reference to the event.
	 *
	 * @param event the event to register.
	 * @param values the values to register.
	 */
	public static void registerEventValues(Event event, Object... values) {
		EVENT_VALUES.put(event, values);
	}

	private boolean isAll;
	private Expression<Number> indexExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		isAll = i == 1;
		indexExpr = (Expression<Number>) exprs[0];
		return true;
	}

	@Override
	protected Object[] get(Event event) {
		Object[] arguments = EVENT_VALUES.getOrDefault(event, new Object[0]);
		if (isAll)
			return arguments;
		else {
			Number number = indexExpr.getSingle(event);
			if (number == null)
				return new Object[0];
			int index = number.intValue() - 1;
			return index >= 0 && arguments.length > index ? new Object[]{arguments[index]} : new Object[0];
		}
	}

	@Override
	public boolean isSingle() {
		return !isAll;
	}

	@Override
	public Class<?> getReturnType() {
		return Object.class;
	}

	@Override
	public Class<? extends Structure>[] getUsableStructures() {
		return new Class[]{StructCommand.class};
	}

	@Override
	public String toString(Event event, boolean b) {
		return isAll ? "all the arguments" : "arg-" + indexExpr.toString(event, b);
	}

}
