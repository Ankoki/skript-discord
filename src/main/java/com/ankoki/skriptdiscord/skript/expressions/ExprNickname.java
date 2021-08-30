package com.ankoki.skriptdiscord.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.skriptdiscord.api.bot.BotManager;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import jdk.jfr.Description;
import jdk.jfr.Name;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

@Name("Member's Nickname")
@Description("Gets and sets the nickname of a discord member")
@Examples("set event-member's nickname to arg-1")
@Since("1.0")
public class ExprNickname extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprNickname.class, String.class, ExpressionType.SIMPLE,
                "%discordmember%[']s nick[name] [using %-discordbot%]");
    }

    private Expression<Member> memberExpr;
    private Expression<DiscordBot> botExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        memberExpr = (Expression<Member>) exprs[0];
        botExpr = exprs.length >= 2 ? (Expression<DiscordBot>) exprs[1] : null;
        return true;
    }

    @Override
    protected String[] get(Event event) {
        Member member = memberExpr.getSingle(event);
        if (member == null) return new String[0];
        return new String[]{member.getNickname()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return memberExpr.toString(e, debug) + "'s nickname";
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        } else if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET) return new Class[0];
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, ChangeMode mode) {
        Member member = memberExpr.getSingle(event);
        if (member != null) {
            String nickname = null;
            if (delta.length >= 1 && delta[0] instanceof String) nickname = (String) delta[0];
            DiscordBot bot = botExpr == null ? BotManager.getFirstBot() :
                    (botExpr.getSingle(event) == null ? BotManager.getFirstBot() : botExpr.getSingle(event));
            if (bot == null) return;
            bot.changeNickname(member, nickname);
        }
    }
}