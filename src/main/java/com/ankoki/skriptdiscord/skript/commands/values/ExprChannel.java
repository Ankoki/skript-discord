package com.ankoki.skriptdiscord.skript.commands.values;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import net.dv8tion.jda.api.entities.MessageChannel;

public class ExprChannel extends EventValueExpression<MessageChannel> {

    static {
        Skript.registerExpression(ExprChannel.class, MessageChannel.class, ExpressionType.SIMPLE,
                "[event[(-| )]][message[(-| )]]channel");
    }

    public ExprChannel() {
        super(MessageChannel.class);
    }

    @Override
    public String toString() {
        return "event-message-channel";
    }
}
