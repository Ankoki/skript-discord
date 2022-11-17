package com.ankoki.skriptdiscord.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.Event;

import java.util.List;

public class SecEmbed extends Section {

	static {
		Skript.registerSection(SecEmbed.class,
				"create [a[n]] embed");
	}

	private String title, description, author;
	private List<MessageEmbed.Field> fields;

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
		this.loadCode(sectionNode);
		return true;
	}

	@Override
	protected TriggerItem walk(Event event) {
		TriggerItem result = walk(event, true);
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(title);
		builder.setDescription(description);
		builder.setAuthor(author);
		for (MessageEmbed.Field field : fields)
			builder.addField(field);
		return result;
	}

	@Override
	public String toString(Event event, boolean b) {
		return "create an embed";
	}

	/**
	 * Gets the current title of the embed.
	 *
	 * @return the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the current embed.
	 *
	 * @param title the new title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the current description of the embed.
	 *
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the current embed.
	 *
	 * @param description the new description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the author of the current embed.
	 *
	 * @return the author.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the author of the current embed.
	 *
	 * @param author the new author.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

}
