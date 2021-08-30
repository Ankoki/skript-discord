package com.ankoki.skriptdiscord.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import jdk.jfr.Description;
import jdk.jfr.Name;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.bukkit.event.Event;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Name("Message")
@Description("Returns a discord message from the id")
@Examples("set {_message} to message with id 7898765456789 from {_channel}")
@Since("1.0")
public class ExprMessage extends SimpleExpression<Message> {

    static {
        Skript.registerExpression(ExprMessage.class, Message.class, ExpressionType.SIMPLE,
                "[text] message with id %number/string% from [[the] channel] %discordchannel%");
    }

    private Expression<Object> objectExpr;
    private Expression<MessageChannel> channelExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        objectExpr = (Expression<Object>) exprs[0];
        channelExpr = (Expression<MessageChannel>) exprs[1];
        return true;
    }

    @Override
    protected Message[] get(Event event) {
        Object object = objectExpr.getSingle(event);
        MessageChannel channel = channelExpr.getSingle(event);
        if (object == null || channel == null) return new Message[0];
        CompletableFuture<Message> future = CompletableFuture.supplyAsync(() -> {
            if (object instanceof Number) return channel.getHistory().getMessageById(((Number) object).longValue());
            else return channel.getHistory().getMessageById((String) object);
        });
        while (!future.isDone()){}
        try {
            return new Message[]{future.get()};
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            Skript.error("Something went horrifically wrong retrieving this message!");
        }
        return new Message[0];
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
