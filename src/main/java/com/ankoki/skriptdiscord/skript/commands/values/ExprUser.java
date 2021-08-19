package com.ankoki.skriptdiscord.skript.commands.values;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;

public class ExprUser extends EventValueExpression<User> {

    static {
        System.out.println("ExprUser static block called");
        Skript.registerExpression(ExprUser.class, User.class, ExpressionType.SIMPLE,
                "[event[(-| )]]user");
    }

    public ExprUser() {
        super(User.class);
    }

    @Override
    public boolean init() {
        boolean test = super.init();
        System.out.println("init = " + test);
        System.out.println("current events = " + Arrays.toString(getParser().getCurrentEvents()));
        return test;
    }

    @Override
    public String toString() {
        return "event-user";
    }
}
