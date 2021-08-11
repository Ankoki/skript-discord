package com.ankoki.skriptdiscord.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.managers.BotManager;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Name("Discord Guild")
@Description("Gets a guild from an id.")
@Examples("set {_guild} to guild with id 747818545466966176")
@Since("1.0")
public class ExprGuild extends SimpleExpression<Guild> {

    static {
        Skript.registerExpression(ExprGuild.class, Guild.class, ExpressionType.SIMPLE,
                "[the] [discord] guild with id %number/string%");
    }

    private Expression<Object> idExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        idExpr = (Expression<Object>) exprs[0];
        return true;
    }

    @Override
    protected Guild[] get(Event event) {
        Object object = idExpr.getSingle(event);
        if (object == null) return new Guild[0];
        Delay.addDelayedEvent(event);
        CompletableFuture<Guild> future = CompletableFuture.supplyAsync(() -> {
            Guild guild = null;
            if (object instanceof String) {
                guild = BotManager.getFirstBot().getJda().getGuildById((String) object);
            } else {
                BotManager.getFirstBot().getJda().getGuildById((String) object);
            }
            return guild;
        });
        while (!future.isDone()){}
        try {
            return new Guild[]{future.get()};
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            Skript.error("Something went horrifically wrong retrieving this guild!");
        }
        return new Guild[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Guild> getReturnType() {
        return Guild.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "guild with id " + idExpr.toString(e, debug);
    }
}
