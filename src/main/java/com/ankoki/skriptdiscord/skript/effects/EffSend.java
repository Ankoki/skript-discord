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
import com.ankoki.skriptdiscord.api.DiscordBot;
import com.ankoki.skriptdiscord.api.DiscordMessage;
import com.ankoki.skriptdiscord.api.managers.BotManager;
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
        Skript.registerEffect(EffSend.class, "(send|message) %discordmessages% to %discorduser/discordmember% [(using %-discordbot%|with discord)]");
    }

    private Expression<DiscordMessage> messageExpr;
    private Expression<Object> receiverExpr;
    private Expression<DiscordBot> botExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        messageExpr = (Expression<DiscordMessage>) exprs[0];
        receiverExpr = (Expression<Object>) exprs[1];
        botExpr = exprs.length == 3 ? (Expression<DiscordBot>) exprs[2] : null;
        return true;
    }

    @Override
    protected void execute(Event event) {
        DiscordMessage message = messageExpr.getSingle(event);
        Object receiver = receiverExpr.getSingle(event);
        DiscordBot bot = botExpr != null ? botExpr.getSingle(event) : BotManager.getFirstBot();
        if (message == null || receiver == null || bot == null) return;
        if (receiver instanceof MessageChannel) bot.sendMessage((MessageChannel) receiver, message);
        else if (receiver instanceof Member) {
            System.out.println("in member");
            bot.sendMessage((Member) receiver, message);
        }
        else bot.sendMessage((User) receiver, message);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "send " + messageExpr.toString(e, debug) + " to " + receiverExpr.toString(e, debug);
    }
}
