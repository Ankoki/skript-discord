package com.ankoki.skriptdiscord.skript.bot.expressions;

import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import jdk.jfr.Description;
import jdk.jfr.Name;

@Name("Bot Description")
@Description("Get's a bots description.")
@Examples("set {_pro-vax} to event-bot's description")
@Since("1.0")
public class ExprDescription extends SimplePropertyExpression<DiscordBot, String> {

    static {
        register(ExprDescription.class, String.class, "description", "discordbots");
    }

    @Override
    public String convert(DiscordBot discordBot) {
        return discordBot.getDescription();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "bot's description";
    }
}