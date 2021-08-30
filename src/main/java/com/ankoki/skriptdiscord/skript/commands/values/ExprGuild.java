package com.ankoki.skriptdiscord.skript.commands.values;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import net.dv8tion.jda.api.entities.Guild;

public class ExprGuild extends EventValueExpression<Guild> {

    static {
        Skript.registerExpression(ExprGuild.class, Guild.class, ExpressionType.SIMPLE,
                "[event[(-| )]]guild");
    }

    public ExprGuild() {
        super(Guild.class);
    }

    @Override
    public String toString() {
        return "event-guild";
    }
}
