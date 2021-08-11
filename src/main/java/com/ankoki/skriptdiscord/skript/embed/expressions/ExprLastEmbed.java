package com.ankoki.skriptdiscord.skript.embed.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.skript.embed.sections.SecEmbed;
import com.ankoki.skriptdiscord.api.DiscordMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.Event;

@Name("Last Embed")
@Description("Returns the last created embed.")
@Examples({"create a new embed:",
        "\tset title to \"My Embed\"",
        "\tset description to \"Hey! This is an embed\"",
        "\tset colour to blue",
        "send last embed to event-user"})
public class ExprLastEmbed extends SimpleExpression<DiscordMessage> {

    static {
        Skript.registerExpression(ExprLastEmbed.class, DiscordMessage.class, ExpressionType.SIMPLE,
                "[the] last [created] embed");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    protected DiscordMessage[] get(Event event) {
        EmbedBuilder builder = SecEmbed.finalBuilder;
        if (builder == null) return new DiscordMessage[0];
        if (builder.isEmpty()) {
            Skript.error("You can't create an empty embed!");
            return new DiscordMessage[0];
        }
        MessageEmbed lastEmbed = builder.build();
        return new DiscordMessage[]{new DiscordMessage(lastEmbed)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends DiscordMessage> getReturnType() {
        return DiscordMessage.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "last created embed";
    }
}
