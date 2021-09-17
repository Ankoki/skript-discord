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
import com.ankoki.skriptdiscord.api.bot.BotManager;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.bukkit.event.Event;

@Name("Discord Message Channel")
@Description("Gets a message channel from an id and a guild.")
@Examples("set {_channel} to channel with id 782734034610552902 from {_guild}")
@Since("1.0")
public class ExprChannel extends SimpleExpression<MessageChannel> {

    static {
        Skript.registerExpression(ExprChannel.class, MessageChannel.class, ExpressionType.SIMPLE,
                "[the] [discord] [(text|message)] channel with id %number/string% (from|in) %discordguild% [using %-discordbot%]");
    }

    private Expression<Object> idExpr;
    private Expression<Guild> guildExpr;
    private Expression<DiscordBot> botExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        idExpr = (Expression<Object>) exprs[0];
        guildExpr = (Expression<Guild>) exprs[1];
        botExpr = exprs.length >= 3 ? (Expression<DiscordBot>) exprs[2] : null;
        return true;
    }

    @Override
    protected MessageChannel[] get(Event event) {
        Object id = idExpr.getSingle(event);
        Guild guild = guildExpr.getSingle(event);
        DiscordBot bot = botExpr == null ? BotManager.getFirstBot() : botExpr.getSingle(event);
        if (id == null || guild == null || bot == null) return new MessageChannel[0];
        Delay.addDelayedEvent(event);
        return new MessageChannel[]{bot.getChannel(guild, String.valueOf(id))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends MessageChannel> getReturnType() {
        return MessageChannel.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "discord channel with id " + idExpr.toString(e, debug) + " in " + guildExpr.toString(e, debug);
    }
}
