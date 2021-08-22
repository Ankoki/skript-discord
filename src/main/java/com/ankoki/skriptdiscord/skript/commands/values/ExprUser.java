package com.ankoki.skriptdiscord.skript.commands.values;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import net.dv8tion.jda.api.entities.User;

public class ExprUser extends EventValueExpression<User> {

    static {
        Skript.registerExpression(ExprUser.class, User.class, ExpressionType.SIMPLE,
                "[event[(-| )]]user");
    }

    public ExprUser() {
        super(User.class);
    }

    @Override
    public String toString() {
        return "event-user";
    }
}
