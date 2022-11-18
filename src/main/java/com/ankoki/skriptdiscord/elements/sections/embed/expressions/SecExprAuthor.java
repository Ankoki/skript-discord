package com.ankoki.skriptdiscord.elements.sections.embed.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.ExpressionType;
import com.ankoki.skriptdiscord.elements.sections.embed.SecEmbed;
import com.ankoki.skriptdiscord.misc.skript.SectionExpression;
import org.bukkit.event.Event;

public class SecExprAuthor extends SectionExpression<String, SecEmbed> {

	static {
		Skript.registerExpression(SecExprAuthor.class, String.class, ExpressionType.SIMPLE,
				"[the] author");
	}

	@Override
	protected String[] get(Event event) {
		return new String[0];
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	@Override
	protected Class<? extends SecEmbed> getSectionType() {
		return SecEmbed.class;
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? new Class<?>[]{String.class} : null;
	}

	@Override
	public void change(Event event, Object[] delta, ChangeMode mode) {
		assert mode == ChangeMode.SET;
		if (delta.length != 1)
			return;
		this.getSection().setAuthor(String.valueOf(delta[0]));
	}

	@Override
	public String toString(Event event, boolean b) {
		return "the author";
	}
}
