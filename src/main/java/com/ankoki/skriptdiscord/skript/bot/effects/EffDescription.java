package com.ankoki.skriptdiscord.skript.bot.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.BotBuilder;
import com.ankoki.skriptdiscord.skript.bot.sections.SecCreateBot;
import com.ankoki.skriptdiscord.utils.Utils;
import org.bukkit.event.Event;

@Name("Bots Name")
@Description("Sets the name of the current bot")
@Examples({"login to a new bot:",
        "\tset name to \"Chat Noir\"",
        "\tset description to \"Miscellaneous utilities.\"",
        "\tset token to \"{@bot-token}\"",
        "\tset activity to watching \"Miraculous Ladybug\"",
        "\tallow intents:",
        "\t\tallow guild messages",
        "\t\tallow direct messages"})
@Since("1.0")
public class EffDescription extends Effect {

    static {
        Skript.registerEffect(EffDescription.class, "set [the] [bot[']s] desc[ription] to %string%");
    }

    private Expression<String> descriptionExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentSection(SecCreateBot.class)) {
            Skript.error("You cannot use this outside of creating a bot!");
            return false;
        }
        descriptionExpr = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        String description = descriptionExpr.getSingle(event);
        if (description == null) return;
        BotBuilder builder = ((SecCreateBot) getParent()).getCurrentBuilder();
        builder.setDescription(description);
        ((SecCreateBot) getParent()).setCurrentBuilder(builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set bots name to " + descriptionExpr.toString(e, debug);
    }
}
