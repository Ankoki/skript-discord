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

@Name("Bots Token")
@Description("Sets the token of the current bot")
@Examples({"login to a new bot:",
        "\tset name to \"Chat Noir\"",
        "\tset description to \"Miscellaneous utilities.\"",
        "\tset token to \"{@bot-token}\"",
        "\tset activity to watching \"Miraculous Ladybug\"",
        "\tallow intents:",
        "\t\tallow guild messages",
        "\t\tallow direct messages"})
@Since("1.0")
public class EffToken extends Effect {

    static {
        Skript.registerEffect(EffToken.class,
                "set [the] [bot[']s] [auth[entication]] token to %string%");
    }

    private Expression<String> tokenExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentSection(SecCreateBot.class)) {
            Skript.error("You cannot use this outside of creating a bot!");
            return false;
        }
        tokenExpr = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        String token = tokenExpr.getSingle(event);
        if (token == null) return;
        BotBuilder builder = ((SecCreateBot) getParent()).getCurrentBuilder();
        builder.setToken(token);
        ((SecCreateBot) getParent()).setCurrentBuilder(builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set bots token to " + tokenExpr.toString(e, debug);
    }
}
