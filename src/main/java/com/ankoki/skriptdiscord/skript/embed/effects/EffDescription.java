package com.ankoki.skriptdiscord.skript.embed.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.skript.embed.sections.SecEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.Event;

@Name("Embeds Description")
@Description("Sets the description of the current embed")
@Examples({"create new embed:",
        "\tset embeds description to \"This is an embed!\", and \"Nicki Minaj is the QUEEEEN of rap.\""})
@Since("1.0")
public class EffDescription extends Effect {

    static {
        Skript.registerEffect(EffDescription.class,
                "set [the] [embed[']s] description to %strings%");
    }

    private Expression<String> descriptionExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentSection(SecEmbed.class)) {
            Skript.error("You cannot use this outside of creating an embed!");
            return false;
        }
        descriptionExpr = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        String[] descriptionArray = descriptionExpr.getArray(event);
        String description = String.join(" ", descriptionArray);
        if (description == null || description.length() > MessageEmbed.DESCRIPTION_MAX_LENGTH) return;
        EmbedBuilder builder = ((SecEmbed) getParent()).getCurrentBuilder();
        builder.setDescription(description);
        ((SecEmbed) getParent()).setCurrentBuilder(builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set embeds description to " + descriptionExpr.toString(e, debug);
    }
}