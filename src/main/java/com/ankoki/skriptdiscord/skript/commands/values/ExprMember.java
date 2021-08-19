package com.ankoki.skriptdiscord.skript.commands.values;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import net.dv8tion.jda.api.entities.Member;

public class ExprMember extends EventValueExpression<Member> {

    static {
        Skript.registerExpression(ExprMember.class, Member.class, ExpressionType.SIMPLE,
                "[event[(-| )]]member");
    }

    public ExprMember() {
        super(Member.class);
    }

    @Override
    public String toString() {
        return "event-member";
    }
}
