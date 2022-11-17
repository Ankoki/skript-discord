package com.ankoki.skriptdiscord.misc.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

import java.util.List;

/**
 * Expression which is only able to be used in a given section.
 * If you need to override the {@link SectionExpression#init(Expression[], int, Kleenean, ParseResult)} for any reason,
 * make sure to call the super first, or you may experience breaking behaviours.
 */
public abstract class SectionExpression<T, S extends Section> extends SimpleExpression<T> {

	private S section;

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, ParseResult parseResult) {
		List<TriggerSection> currentSections = this.getParser().getCurrentSections();
		if (currentSections.isEmpty() || currentSections.get(currentSections.size() - 1).getClass() != this.getSectionType()) {
			Skript.error("You cannot use this expression outside of a '" + this.toString(null, false) + "' section.");
			return false;
		}
		this.section = (S) currentSections.get(currentSections.size() - 1);
		return true;
	}

	/**
	 * Gets the class of the section this expression is usable in.
	 * @return the section in which this expression is able to be used.
	 */
	protected abstract Class<? extends S> getSectionType();

	/**
	 * Gets the current section.
	 * @return the current section.
	 */
	public S getSection() {
		return section;
	}

}
