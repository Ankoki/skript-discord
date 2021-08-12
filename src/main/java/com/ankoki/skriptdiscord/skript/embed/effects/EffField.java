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
import org.bukkit.event.Event;

@Name("Embeds Field")
@Description("Adds a field to the current embed.")
@Examples({"create new embed:",
        "\tadd field titled \"hello\" with value \"nicki minaj is great\""})
@Since("1.0")
public class EffField extends Effect {

    static {
        Skript.registerEffect(EffField.class,
                "add [a[n]] (1¦inline|) field [with] title[d] %string% (and|with) [the] [(value|text)] %strings%");
    }

    private Expression<String> titleExpr, valueExpr;
    private boolean inline;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentSection(SecEmbed.class)) {
            Skript.error("You cannot use this outside of creating an embed!");
            return false;
        }
        titleExpr = (Expression<String>) exprs[0];
        valueExpr = (Expression<String>) exprs[1];
        inline = parseResult.mark == 1;
        return true;
    }

    @Override
    protected void execute(Event event) {
        String title = titleExpr.getSingle(event);
        String value = String.join("\n", valueExpr.getArray(event));
        if (title == null) return;
        EmbedBuilder builder = ((SecEmbed) getParent()).getCurrentBuilder();
        builder.addField(title, value, inline);
        ((SecEmbed) getParent()).setCurrentBuilder(builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add" + (inline ? " inline" : "") + " field titled " + titleExpr.toString(e, debug) +
                "with value " + valueExpr.toString(e, debug);
    }
}
