package com.ankoki.skriptdiscord.elements.sections.embed.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.elements.sections.embed.SecEmbed;
import com.ankoki.skriptdiscord.misc.Misc;
import com.ankoki.skriptdiscord.misc.skript.SectionExpression;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import org.bukkit.event.Event;

public class SecExprField extends SectionExpression<Field, SecEmbed> {

	static {
		Skript.registerExpression(SecExprField.class, Field.class, ExpressionType.SIMPLE,
				"[the] [current] fields",
				"[:inline] field (name|title)d %string% with content %strings%");
	}

	private Expression<String> nameExpr, valueExpr;
	private boolean isCreation, isInline;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		if (!super.init(exprs, i, kleenean, parseResult))
			return false;
		if (i == 1) {
			isCreation = true;
			isInline = parseResult.hasTag("inline");
			nameExpr = (Expression<String>) exprs[0];
			valueExpr = (Expression<String>) exprs[1];
		}
		return true;
	}

	@Override
	protected Field[] get(Event event) {
		Field[] fields = this.getSection().getFields().toArray(new Field[0]);
		if (isCreation) {
			String name = nameExpr.getSingle(event);
			String value = String.join("\n", valueExpr.getAll(event));
			if (Misc.isDiscordSafe(name, Misc.TextType.TITLE) && Misc.isDiscordSafe(value, Misc.TextType.VALUE))
				fields = new Field[]{new Field(name, value, isInline)};
			else
				fields = new Field[0];
		}
		return fields;
	}

	@Override
	public boolean isSingle() {
		return !isCreation;
	}

	@Override
	public Class<? extends Field> getReturnType() {
		return Field.class;
	}

	@Override
	protected Class<? extends SecEmbed> getSectionType() {
		return SecEmbed.class;
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.ADD ? new Class[]{Field.class} : null;
	}

	@Override
	public void change(Event event, Object[] delta, ChangeMode mode) {
		assert mode == ChangeMode.ADD;
		if (delta.length != 1 || !(delta[0] instanceof Field field))
			return;
		this.getSection().addField(field);
	}

	@Override
	public String toString(Event event, boolean b) {
		return isCreation ? (isInline ? "inline " : "") + "field named " + nameExpr.toString(event, b) +
				" with content " + valueExpr.toString(event, b) :
				"the current fields";
	}

}
