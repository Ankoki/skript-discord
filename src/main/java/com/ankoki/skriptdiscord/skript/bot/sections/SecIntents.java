package com.ankoki.skriptdiscord.skript.bot.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.Kleenean;
import com.ankoki.skriptdiscord.api.bot.BotBuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.event.Event;

import java.util.List;

@Name("Intents Section")
@Description("Used to allow bots to have intents.")
@Examples({"login to a new bot:",
        "\tset name to \"Cat Noir\"",
        "\tset description to \"Hey! I'm Chat Noir... Yeah. Chat Noir !\"",
        "\tset token to \"{@bot-token}\"",
        "\tallow intents:",
        "\t\tallow guild messages",
        "\t\tallow direct messages"})
@Since("1.0")
public class SecIntents extends EffectSection {

    static {
        Skript.registerSection(SecIntents.class,
                "allow (1¦all|) intents");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        if (!ParserInstance.get().isCurrentSection(SecCreateBot.class)) {
            Skript.error("You can only allow intents in a bot creation section!");
            return false;
        }
        if (parseResult.mark == 1) {
            if (hasSection()) {
                Skript.error("You cannot enable all intents and then list more!");
                return false;
            }
        }
        return true;
    }

    @Override
    protected TriggerItem walk(Event event) {
        if (!hasSection()) {
            BotBuilder builder = ((SecCreateBot) getParent()).getCurrentBuilder();
            builder.allowIntent(GatewayIntent.values());
            ((SecCreateBot) getParent()).setCurrentBuilder(builder);
        }
        return walk(event, true);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "allow intents";
    }
}
