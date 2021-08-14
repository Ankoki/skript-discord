package com.ankoki.skriptdiscord.skript.bot.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.bot.BotBuilder;
import com.ankoki.skriptdiscord.skript.bot.sections.SecCreateBot;
import org.bukkit.event.Event;

@Name("Bots Prefix")
@Description("Sets a bots prefix.")
@Examples({"login to a new bot:",
        "\tset name to \"Chat Noir\"",
        "\tset description to \"Miscellaneous utilities.\"",
        "\tset token to \"{@bot-token}\"",
        "\tallow intents:",
        "\t\tallow guild messages",
        "\t\tallow direct messages",
        "\tset prefix to \"!\"",
        "\tlogin to this bot"})
@Since("1.0")
public class EffPrefix extends Effect {

    static {
        Skript.registerEffect(EffPrefix.class, "set [the] [bot[']s] [command] prefix to %string%");
    }

    private Expression<String> prefixExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentSection(SecCreateBot.class)) {
            Skript.error("You cannot use this outside of creating a bot!");
            return false;
        }
        prefixExpr = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        String prefix = prefixExpr.getSingle(event);
        if (prefix == null) return;
        BotBuilder builder = ((SecCreateBot) getParent()).getCurrentBuilder();
        builder.setPrefix(prefix);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "login to this discord bot";
    }
}