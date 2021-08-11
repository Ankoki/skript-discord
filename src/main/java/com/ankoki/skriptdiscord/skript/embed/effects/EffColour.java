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
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.skript.embed.sections.SecEmbed;
import com.ankoki.skriptdiscord.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

import java.awt.*;

@Name("Embeds Colour")
@Description("Sets the colour of the current embed")
@Examples({"create new embed:",
        "\tset embeds colour to blue"})
@Since("1.0")
public class EffColour extends Effect {

    static {
        Skript.registerEffect(EffColour.class, "set [the] [embed[']s] colo[u]r to [rgb[ ][(]]%number%, %number%, %number%[)]");
    }

    private Expression<Number> redExpr;
    private Expression<Number> greenExpr;
    private Expression<Number> blueExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentSection(SecEmbed.class)) {
            Skript.error("You cannot use this outside of creating an embed!");
            return false;
        }
        redExpr = (Expression<Number>) exprs[0];
        greenExpr = (Expression<Number>) exprs[1];
        blueExpr = (Expression<Number>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Number red = redExpr.getSingle(event);
        Number green = greenExpr.getSingle(event);
        Number blue = blueExpr.getSingle(event);
        if (red == null || green == null || blue == null) return;
        Color colour = new Color(Math.min(255, Math.max(0, red.intValue())),
                Math.min(255, Math.max(0, green.intValue())),
                Math.min(255, Math.max(0, blue.intValue())));
        EmbedBuilder builder = ((SecEmbed) getParent()).getCurrentBuilder();
        builder.setColor(colour);
        ((SecEmbed) getParent()).setCurrentBuilder(builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set embeds colour to rgb " + redExpr.toString(e, debug) + ", " +
                blueExpr.toString(e, debug) + ", " +
                greenExpr.toString(e, debug);
    }
}
