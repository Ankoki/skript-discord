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
import com.ankoki.skriptdiscord.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.Event;

@Name("Embeds Title")
@Description("Sets the title of the current embed")
@Examples({"create new embed:",
        "\tset embeds title to \"Hello!\""})
@Since("1.0")
public class EffTitle extends Effect {

    static {
        Skript.registerEffect(EffTitle.class, "set [the] [embed[']s] title to %string%");
    }

    private Expression<String> titleExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentSection(SecEmbed.class)) {
            Skript.error("You cannot use this outside of creating an embed!");
            return false;
        }
        titleExpr = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        String title = titleExpr.getSingle(event);
        if (title == null || title.length() > MessageEmbed.TITLE_MAX_LENGTH) return;
        EmbedBuilder builder = ((SecEmbed) getParent()).getCurrentBuilder();
        builder.setTitle(title);
        ((SecEmbed) getParent()).setCurrentBuilder(builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set embeds title to " + titleExpr.toString(e, debug);
    }
}
