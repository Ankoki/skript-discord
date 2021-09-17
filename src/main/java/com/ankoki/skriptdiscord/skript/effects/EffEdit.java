package com.ankoki.skriptdiscord.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.DiscordMessage;
import com.ankoki.skriptdiscord.api.bot.BotManager;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import com.ankoki.skriptdiscord.utils.Console;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Edit")
@Description("Edit's a discord message the bot has sent. Must be the author.")
@Examples("edit {_message} to \"Whoops, I made a spelling mistake!\"")
@Since("1.0")
public class EffEdit extends Effect {

    static {
        Skript.registerEffect(EffEdit.class,
                "edit %discordmessage% to [be] %skdiscordmessage/string% [using %-discordbot%]");
    }

    private Expression<Message> messageExpr;
    private Expression<Object> replyExpr;
    private Expression<DiscordBot> botExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        messageExpr = (Expression<Message>) exprs[0];
        replyExpr = (Expression<Object>) exprs[1];
        botExpr = exprs.length >= 3 ? (Expression<DiscordBot>) exprs[2] : null;
        return true;
    }

    @Override
    protected void execute(Event event) {
        Message message = messageExpr.getSingle(event);
        Object replyObject = replyExpr.getSingle(event);
        DiscordBot bot = botExpr == null ? BotManager.getFirstBot() : botExpr.getSingle(event);
        if (message == null || replyObject == null || bot == null) return;
        DiscordMessage discordMessage = new DiscordMessage(replyObject);
        bot.edit(message, discordMessage);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "edit " + messageExpr.toString(e, debug) + " to " + replyExpr.toString(e, debug) +
                (botExpr == null ? "" : " using " + botExpr.toString(e, debug));
    }
}
