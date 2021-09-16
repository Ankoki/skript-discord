package com.ankoki.skriptdiscord.skript.bot.expressions;

import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import jdk.jfr.Description;
import jdk.jfr.Name;

@Name("Bot Name")
@Description("Get's a bots name.")
@Examples("set {_pro-vax} to event-bot's name")
@Since("1.0")
public class ExprName extends SimplePropertyExpression<DiscordBot, String> {

    static {
        register(ExprName.class, String.class, "name", "discordbots");
    }

    @Override
    public String convert(DiscordBot discordBot) {
        return discordBot.getName();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "bot's name";
    }
}