package com.ankoki.skriptdiscord.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.DiscordMessage;
import com.ankoki.skriptdiscord.api.bot.BotManager;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import com.ankoki.skriptdiscord.utils.Console;
import jdk.jfr.Description;
import jdk.jfr.Name;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Reply")
@Description("Replies to a discord message with the given content.")
@Examples("reply to {_message} with last embed")
@Since("1.0")
public class EffReply extends Effect {

    static {
        Skript.registerEffect(EffReply.class,
                "[[skript-]discord] reply to %discordmessage% with %skdiscordmessage/string% [using %-discordbot%]");
        Console.debug("EffReply static");
    }

    private Expression<Message> messageExpr;
    private Expression<Object> replyExpr;
    private Expression<DiscordBot> botExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        messageExpr = (Expression<Message>) exprs[0];
        replyExpr = (Expression<Object>) exprs[1];
        botExpr = exprs.length >= 3 ? (Expression<DiscordBot>) exprs[2] : null;
        Console.debug("EffReply init");
        return true;
    }

    @Override
    protected void execute(Event event) {
        Message message = messageExpr.getSingle(event);
        Object object = replyExpr.getSingle(event);
        DiscordBot bot = botExpr == null ? BotManager.getFirstBot() : botExpr.getSingle(event);
        if (message == null || object == null || bot == null) return;
        DiscordMessage reply = new DiscordMessage(object);
        bot.replyTo(message, reply);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "reply to " + messageExpr.toString(e, debug) + " with " + replyExpr.toString(e, debug) + (botExpr == null ? "" : " using " + replyExpr.toString(e, debug));
    }
}
