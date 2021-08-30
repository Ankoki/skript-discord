package com.ankoki.skriptdiscord.skript.bot.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.bot.BotManager;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import jdk.jfr.Description;
import jdk.jfr.Name;
import org.bukkit.event.Event;

@Name("Discord Bot")
@Description("Gets a bot by its name.")
@Examples("set {_bot} to discord bot named \"jimmy\"")
@Since("1.0")
public class ExprBot extends SimpleExpression<DiscordBot> {

    static {
        Skript.registerExpression(ExprBot.class, DiscordBot.class, ExpressionType.SIMPLE,
                "[discord] bot named %string%");
    }

    private Expression<String> nameExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        nameExpr = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected DiscordBot[] get(Event event) {
        String name = nameExpr.getSingle(event);
        if (name == null) return new DiscordBot[0];
        DiscordBot bot = BotManager.getBot(name);
        return bot == null ? new DiscordBot[0] : new DiscordBot[]{bot};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends DiscordBot> getReturnType() {
        return DiscordBot.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "discord bot named " + nameExpr.toString(e, debug);
    }
}
