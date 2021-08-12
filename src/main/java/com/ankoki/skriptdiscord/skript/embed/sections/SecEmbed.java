package com.ankoki.skriptdiscord.skript.embed.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

import java.util.List;

@Name("Embed Section")
@Description("Creates a new embed.")
@Examples({"create a new embed:",
        "\tset title to \"My Embed\"",
        "\tset description to \"Hey! This is an embed\"",
        "\tset colour to blue"})
@Since("1.0")
public class SecEmbed extends Section {

    static {
        Skript.registerSection(SecEmbed.class,
                "(create|make) [a[n]] [new] embed");
    }

    public static EmbedBuilder finalBuilder = new EmbedBuilder();
    private EmbedBuilder currentBuilder = new EmbedBuilder();

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        loadCode(sectionNode);
        return true;
    }

    @Override
    protected TriggerItem walk(Event event) {
        currentBuilder = new EmbedBuilder();
        TriggerItem walk = walk(event, true);
        finalBuilder = currentBuilder;
        return walk;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create embed";
    }

    public EmbedBuilder getCurrentBuilder() {
        return currentBuilder;
    }

    public void setCurrentBuilder(EmbedBuilder currentBuilder) {
        this.currentBuilder = currentBuilder;
    }
}
