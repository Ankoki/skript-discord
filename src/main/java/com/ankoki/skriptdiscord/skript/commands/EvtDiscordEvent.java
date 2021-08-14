package com.ankoki.skriptdiscord.skript.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.validate.SectionValidator;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.log.SkriptLogger;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Discord Command")
@Description("Register a discord command with Skript.")
@Examples({"discord command repeat <text>:",
            "\tsend arg-1 to event-channel using event-bot"})
@Since("1.0")
public class EvtDiscordEvent extends SkriptEvent {

    static {
        Skript.registerEvent("Discord Command", EvtDiscordEvent.class, BukkitDiscordCommandEvent.class,
                "discord command <([^\\\\s]+)( .+)?$>");
    }

    private static final String listPattern = "\\s*,\\s*|\\s+(and|or|, )\\s+";
    private static final SectionValidator commandStructure = new SectionValidator()
            .addEntry("usage", true)
            .addEntry("description", true)
            .addEntry("roles", true)
            .addEntry("aliases", true)
            .addEntry("bots", true)
            .addEntry("executable in", true)
            .addEntry("permissions", true)
            .addEntry("permission message", true)
            .addEntry("guild cooldown", true)
            .addEntry("cooldown message", true)
            .addSection("trigger", false);

    private String commandName;
    private String unparsedArguments;
    private List<String> aliases = new ArrayList<>();

    public boolean init(Literal<?>[] exprs, int matchedPattern, ParseResult parseResult) {
        SectionNode node = (SectionNode) SkriptLogger.getNode();

        if (node == null) return false;
        if (!commandStructure.validate(node)) {
            Skript.error("There is something wrong with your command! You must have a trigger.");
            return false;
        }

        commandName = parseResult.regexes.get(0).group(1);
        unparsedArguments = parseResult.regexes.get(0).group(2);

        String fullPattern = node.getKey();
        if (fullPattern == null) return false;
        int count = 0;
        for (int i = 0; i < fullPattern.length(); i++) {
            if (fullPattern.charAt(i) == '[') {
                count++;
            } else if (fullPattern.charAt(i) == ']') {
                if (count <= 0) {
                    Skript.error("You cannot close optional brackets without opening them!");
                    return false;
                }
                count--;
            }
        }
        if (count >= 1) {
            Skript.error("You cannot open optional brackets without closing them!");
            return false;
        } else if (!(node.get("trigger") instanceof SectionNode)) {
            Skript.error("The trigger needs to be a section!");
            return false;
        }

        String rawAliases = node.get("aliases", "");
        if (!rawAliases.equals("")) {
            aliases = Arrays.asList(rawAliases.split(", "));
        }
        aliases.add(commandName);

        // TODO finish lol, parse arguments and stuff

        String registered = CommandManager.get().isRegistered(commandName);
        if (registered != null) {
            Skript.error("Discord command " + commandName + " is already registered! (" + registered + ")");
            return false;
        }
        CommandManager.get().addCommand(node.getConfig().getFile(), commandName);
        return true;
    }

    @Override
    public boolean check(Event e) {
        BukkitDiscordCommandEvent event = (BukkitDiscordCommandEvent) e;
        DiscordCommand command = event.getCommand();
        if (aliases.contains(command.getUsedAlias())) {
            // TODO check if arguments match and shit blah blah idk how imma do that </3
        }
        return false;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "discord command " + commandName + (unparsedArguments == null ? "" : " " + unparsedArguments);
    }
}
