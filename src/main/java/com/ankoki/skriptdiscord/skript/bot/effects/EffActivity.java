package com.ankoki.skriptdiscord.skript.bot.effects;

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
import com.ankoki.skriptdiscord.api.BotBuilder;
import com.ankoki.skriptdiscord.skript.bot.sections.SecCreateBot;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.event.Event;

@Name("Bots Name")
@Description("Sets the name of the current bot")
@Examples({"login to a new bot:",
        "\tset name to \"Chat Noir\"",
        "\tset description to \"Miscellaneous utilities.\"",
        "\tset token to \"{@bot-token}\"",
        "\tset activity to watching \"Miraculous Ladybug\"",
        "\tallow intents:",
        "\t\tallow guild messages",
        "\t\tallow direct messages"})
@Since("1.0")
public class EffActivity extends Effect {

    static {
        Skript.registerEffect(EffActivity.class,
                "set [the] [bot[']s] activity to (streaming|1¦watching|2¦playing|3¦listening|4¦competing) %string% [at %-string%]");
    }

    private Expression<String> activityExpr;
    private Expression<String> urlExpr;
    private int mark;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentSection(SecCreateBot.class)) {
            Skript.error("You cannot use this outside of creating a bot!");
            return false;
        }
        activityExpr = (Expression<String>) exprs[0];
        if (exprs.length >= 2 && parseResult.mark == 0) {
            Skript.error("You cannot provide a streaming link unless you are using the streaming activity!");
            return false;
        }
        else if (parseResult.mark == 0) {
            urlExpr = (Expression<String>) exprs[1];
        }
        mark = parseResult.mark;
        return true;
    }

    @Override
    protected void execute(Event event) {
        String string = activityExpr.getSingle(event);
        if (string == null) return;
        Activity activity = null;
        switch (mark) {
            case 0:
                String url = urlExpr.getSingle(event);
                if (!Activity.isValidStreamingUrl(url)) {
                    Skript.error("skript-discord | Invalid streaming url!");
                    return;
                }
                activity = Activity.streaming(string, url);
                break;
            case 1:
                activity = Activity.watching(string);
                break;
            case 2:
                activity = Activity.playing(string);
                break;
            case 3:
                activity = Activity.listening(string);
                break;
            case 4:
                activity = Activity.competing(string);
        }
        assert activity != null;
        BotBuilder builder = ((SecCreateBot) getParent()).getCurrentBuilder();
        builder.setActivity(activity);
        ((SecCreateBot) getParent()).setCurrentBuilder(builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set bots name to " + activityExpr.toString(e, debug);
    }
}
