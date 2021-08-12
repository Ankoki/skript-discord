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
import com.ankoki.skriptdiscord.api.DiscordBot;
import com.ankoki.skriptdiscord.api.managers.BotManager;
import org.bukkit.event.Event;

@Name("Shutdown Bot")
@Description("Logs out of a bot and unregisters it.")
@Examples("shutdown bot named \"jack and jill\"")
@Since("1.0")
public class EffShutdown extends Effect {

    static {
        Skript.registerEffect(EffShutdown.class,
                "(shutdown|log[ ]out of) [the] bot [named] %discordbot/string%");
    }

    private Expression<Object> objectExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        objectExpr = (Expression<Object>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Object obj = objectExpr.getSingle(event);
        if (obj == null) return;
        else if (obj instanceof String) BotManager.disable((String) obj);
        else BotManager.disable((DiscordBot) obj);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "shutdown bot " + objectExpr.toString(e, debug);
    }
}