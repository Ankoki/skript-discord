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
import com.ankoki.skriptdiscord.skript.bot.sections.SecIntents;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.event.Event;

@Name("Bots Intent")
@Description("Adds an allowed intent to the current bot")
@Examples({"login to a new bot:",
        "\tset name to \"Chat Noir\"",
        "\tset description to \"Miscellaneous utilities.\"",
        "\tset token to \"{@bot-token}\"",
        "\tset activity to watching \"Miraculous Ladybug\"",
        "\tallow intents:",
        "\t\tallow guild messages",
        "\t\tallow direct messages"})
@Since("1.0")
public class EffAllowIntent extends Effect {

    static {
        Skript.registerEffect(EffAllowIntent.class,
                "allow [the] intent %discordintent%");
    }

    private Expression<GatewayIntent> intentExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentSection(SecIntents.class)) {
            Skript.error("You cannot use this outside of an allow intents section!");
            return false;
        }
        intentExpr = (Expression<GatewayIntent>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        GatewayIntent intent = intentExpr.getSingle(event);
        if (intent == null) return;
        BotBuilder builder = ((SecCreateBot) ((SecIntents) getParent()).getParent()).getCurrentBuilder();
        builder.allowIntent(intent);
        ((SecCreateBot) getParent()).setCurrentBuilder(builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "allow intent " + intentExpr.toString(e, debug);
    }
}
