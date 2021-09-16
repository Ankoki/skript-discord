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
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import com.ankoki.skriptdiscord.api.DiscordMessage;
import com.ankoki.skriptdiscord.api.bot.BotManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

@Name("Send Message")
@Description("Sends a discord message to a channel or user with a bot.")
@Examples("send \"coz i be the baddie b\" to event-user")
@Since("1.0")
public class EffSend extends Effect {

    static {
        Skript.registerEffect(EffSend.class, "[[skript-]discord] send %skdiscordmessages/strings% to %discorduser/discordmember/discordchannel% [using %-discordbot%]");
    }

    private Expression<Object> messageExpr;
    private Expression<Object> receiverExpr;
    private Expression<DiscordBot> botExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        messageExpr = (Expression<Object>) exprs[0];
        receiverExpr = (Expression<Object>) exprs[1];
        botExpr = exprs.length == 3 ? (Expression<DiscordBot>) exprs[2] : null;
        return true;
    }

    @Override
    protected void execute(Event event) {
        Object message = messageExpr.getSingle(event);
        Object receiver = receiverExpr.getSingle(event);
        DiscordBot bot = botExpr != null ? botExpr.getSingle(event) : BotManager.getFirstBot();
        if (message == null || receiver == null || bot == null) return;
        if (receiver instanceof MessageChannel) bot.sendMessage((MessageChannel) receiver, message instanceof String ?
                new DiscordMessage((String) message) : (DiscordMessage) message);
        else if (receiver instanceof Member) bot.sendMessage((Member) receiver, message instanceof String ?
                new DiscordMessage((String) message) : (DiscordMessage) message);
        else bot.sendMessage((User) receiver, message instanceof String ?
                    new DiscordMessage((String) message) : (DiscordMessage) message);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "send " + messageExpr.toString(e, debug) + " to " + receiverExpr.toString(e, debug);
    }
}
