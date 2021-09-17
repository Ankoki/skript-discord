package com.ankoki.skriptdiscord.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.bot.BotManager;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import jdk.jfr.Description;
import jdk.jfr.Name;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.bukkit.event.Event;

@Name("Message")
@Description("Returns a discord message from the id")
@Examples("set {_message} to message with id 7898765456789 from {_channel}")
@Since("1.0")
public class ExprMessage extends SimpleExpression<Message> {

    static {
        Skript.registerExpression(ExprMessage.class, Message.class, ExpressionType.SIMPLE,
                "[text] message with id %number/string% from [[the] channel] %discordchannel% [using %-discordbot%]");
    }

    private Expression<Object> objectExpr;
    private Expression<MessageChannel> channelExpr;
    private Expression<DiscordBot> botExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        objectExpr = (Expression<Object>) exprs[0];
        channelExpr = (Expression<MessageChannel>) exprs[1];
        botExpr = exprs.length >= 3 ? (Expression<DiscordBot>) exprs[2] : null;
        return true;
    }

    @Override
    protected Message[] get(Event event) {
        Object object = objectExpr.getSingle(event);
        MessageChannel channel = channelExpr.getSingle(event);
        DiscordBot bot = botExpr == null ? BotManager.getFirstBot() : botExpr.getSingle(event);
        if (object == null || channel == null || bot == null) return new Message[0];
        Delay.addDelayedEvent(event);
        return new Message[]{bot.getMessage(channel, String.valueOf(object))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Message> getReturnType() {
        return Message.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "message with id " + objectExpr.toString(e, debug) + " from " + channelExpr.toString(e, debug);
    }
}
