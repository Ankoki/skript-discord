package com.ankoki.skriptdiscord.skript.bot.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.BotBuilder;
import org.bukkit.event.Event;

import java.util.List;

@Name("Bot Section")
@Description("Logs into a new bot.")
@Examples({"login to a new bot:",
        "\tset name to \"Chat Noir\"",
        "\tset description to \"Miscellaneous utilities.\"",
        "\tset token to \"{@bot-token}\"",
        "\tallow intents:",
        "\t\tallow guild messages",
        "\t\tallow direct messages"})
@Since("1.0")
public class SecCreateBot extends Section {

    static {
        Skript.registerSection(SecCreateBot.class,
                "(create|make|login [to]) [a] [new] bot");
    }

    private BotBuilder currentBuilder = new BotBuilder();

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        currentBuilder = new BotBuilder();
        loadCode(sectionNode);
        return true;
    }

    @Override
    protected TriggerItem walk(Event event) {
        return walk(event, true);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "login to bot";
    }

    public BotBuilder getCurrentBuilder() {
        return currentBuilder;
    }

    public void setCurrentBuilder(BotBuilder currentBuilder) {
        this.currentBuilder = currentBuilder;
    }
}
