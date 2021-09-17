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
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import com.ankoki.skriptdiscord.api.bot.BotManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

@Name("Discord Member")
@Description("Gets a member from an id.")
@Examples("set {_member} to member with id 722898458020937769 from {_guild}")
@Since("1.0")
public class ExprMember extends SimpleExpression<Member> {

    static {
        Skript.registerExpression(ExprMember.class, Member.class, ExpressionType.SIMPLE,
                "[the] [discord] member with id %number/string% from %discordguild% [using %-discordbot%]");
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
    protected Member[] get(Event event) {
        Object object = idExpr.getSingle(event);
        Guild guild = guildExpr.getSingle(event);
        DiscordBot bot = botExpr == null ? BotManager.getFirstBot() : botExpr.getSingle(event);
        if (guild == null || object == null || bot == null) return new Member[0];
        Delay.addDelayedEvent(event);
        return new Member[]{bot.getMember(guild, String.valueOf(object))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Member> getReturnType() {
        return Member.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "member with id " + idExpr.toString(e, debug) + " from " + guildExpr.toString(e, debug);
    }
}
