package com.ankoki.skriptdiscord.skript.bot.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.BotBuilder;
import com.ankoki.skriptdiscord.api.managers.BotManager;
import com.ankoki.skriptdiscord.skript.bot.sections.SecCreateBot;
import org.bukkit.event.Event;

@Name("Login Bot")
@Description("Logs into the last created bot.")
@Examples({"login to a new bot:",
        "\tset name to \"Chat Noir\"",
        "\tset description to \"Miscellaneous utilities.\"",
        "\tset token to \"{@bot-token}\"",
        "\tallow intents:",
        "\t\tallow guild messages",
        "\t\tallow direct messages",
        "login to last created bot"})
@Since("1.0")
public class EffLogin extends Effect {

    static {
        Skript.registerEffect(EffLogin.class, "login to [the] last [created] [discord] bot");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    protected void execute(Event e) {
        BotBuilder builder = SecCreateBot.finalBuilder;
        if (builder == null) {
            Skript.error("skript-discord | You cannot login to a bot without creating one!");
        }
        else if (builder.init()) {
            BotManager.registerBot(builder.build());
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "login to last bot";
    }
}
