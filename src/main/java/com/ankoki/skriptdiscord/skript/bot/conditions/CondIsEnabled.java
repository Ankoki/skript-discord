package com.ankoki.skriptdiscord.skript.bot.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.ankoki.skriptdiscord.api.managers.BotManager;

@Name("Is Bot Enabled")
@Description("Checks if a bot is enabled or not.")
@Examples("if \"beyonce\" is logged in to discord:")
@Since("1.0")
public class CondIsEnabled extends PropertyCondition<String> {

    static {
        register(CondIsEnabled.class,
                "(enabled|loaded|registered|logged in) (on|to) discord", "string");
    }

    @Override
    public boolean check(String name) {
        if (name == null) return false;
        return BotManager.isRegistered(name);
    }

    @Override
    protected String getPropertyName() {
        return "enabled";
    }
}
