package com.ankoki.skriptdiscord.skript.commands;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.command.Argument;
import ch.njol.skript.config.EntryNode;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.validate.SectionValidator;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.Utils;
import ch.njol.util.NonNullPair;
import ch.njol.util.StringUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("Discord Command")
@Description("Register a discord command with Skript.")
@Examples({"discord command repeat <text>:",
        "\tprefixes: . and !",
        "\taliases: echo",
        "\tdescription: Repeats the text that was sent.",
        "\ttrigger:",
        "\t\tsend arg-1 to event-channel using event-bot"})
@Since("1.0")
public class EvtDiscordEvent extends SkriptEvent {

    static {
        Skript.registerEvent("Discord Command", EvtDiscordEvent.class, BukkitDiscordCommandEvent.class,
                "discord command <([^\\s]+)( .+)?$>");

        // TODO set arguments, not sure how i'm going to be able to set the arguments as Skript doesn't do it like this -.-

        EventValues.registerEventValue(BukkitDiscordCommandEvent.class, Member.class, new Getter<Member, BukkitDiscordCommandEvent>() {
            @Override
            public Member get(BukkitDiscordCommandEvent event) {
                return event.getCommand().getMember();
            }
        }, 0);

        EventValues.registerEventValue(BukkitDiscordCommandEvent.class, User.class, new Getter<User, BukkitDiscordCommandEvent>() {
            @Override
            public User get(BukkitDiscordCommandEvent event) {
                return event.getCommand().getMember().getUser();
            }
        }, 0);

        EventValues.registerEventValue(BukkitDiscordCommandEvent.class, MessageChannel.class, new Getter<MessageChannel, BukkitDiscordCommandEvent>() {
            @Override
            public MessageChannel get(BukkitDiscordCommandEvent event) {
                return event.getCommand().getChannel();
            }
        }, 0);
    }

    private static final Pattern argumentPattern = Pattern.compile("<\\s*(?:(.+?)\\s*:\\s*)?(.+?)\\s*(?:=\\s*(" + SkriptParser.wildcard + "))?\\s*>");
    private final static Pattern escape = Pattern.compile("[" + Pattern.quote("(|)<>%\\") + "]");
    private static final String listPattern = "\\s*,\\s*|\\s+(and|or|, )\\s+";
    private static final SectionValidator commandStructure = new SectionValidator()
            .addEntry("aliases", true)
            .addEntry("description", true)
            .addEntry("roles", true)
            .addEntry("bots", true)
            .addEntry("executable in", true)
            .addEntry("permissions", true)
            .addEntry("permission message", true)
            .addEntry("guild cooldown", true)
            .addEntry("cooldown message", true)
            .addEntry("prefixes", false)
            .addSection("trigger", false);

    private String commandName;
    private String unparsedArguments;
    private final List<Argument<?>> currentArguments = new ArrayList<>();
    private final List<String> prefixes = new ArrayList<>();
    // TODO all the entries such as description etc
    private final List<String> aliases = new ArrayList<>();
    private Trigger trigger;

    private static String escape(final String s) {
        return escape.matcher(s).replaceAll("\\\\$0");
    }

    public boolean init(Literal<?>[] exprs, int matchedPattern, ParseResult parseResult) {
        SectionNode node = (SectionNode) SkriptLogger.getNode();

        if (node == null) return false;
        node.convertToEntries(0);
        if (!commandStructure.validate(node)) {
            return false;
        } else if (!(node.get("trigger") instanceof SectionNode)) {
            Skript.error("The trigger needs to be a section!");
            return false;
        } else if (!(node.get("prefixes") instanceof EntryNode)) {
            Skript.error("The prefixes cannot be a section!");
            return false;
        }

        String rawPrefixes = ScriptLoader.replaceOptions(node.get("prefixes", ""));
        Collections.addAll(prefixes, rawPrefixes.split(listPattern));

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
        }

        String rawAliases = ScriptLoader.replaceOptions(node.get("aliases", ""));
        if (!rawAliases.equals("")) {
            Collections.addAll(aliases, rawAliases.split(listPattern));
        }

        aliases.add(commandName);

        if (unparsedArguments != null) {
            StringBuilder pattern = new StringBuilder();
            Matcher matcher = argumentPattern.matcher(unparsedArguments);
            int lastEnd = 0;
            int optionals = 0;
            for (int i = 0; matcher.find(); i++) {
                pattern.append(escape("" + unparsedArguments.substring(lastEnd, matcher.start())));
                optionals += StringUtils.count(unparsedArguments, '[', lastEnd, matcher.start());
                optionals -= StringUtils.count(unparsedArguments, ']', lastEnd, matcher.start());

                lastEnd = matcher.end();

                ClassInfo<?> classInfo;
                classInfo = Classes.getClassInfoFromUserInput("" + matcher.group(2));
                NonNullPair<String, Boolean> pair = Utils.getEnglishPlural("" + matcher.group(2));
                if (classInfo == null)
                    classInfo = Classes.getClassInfoFromUserInput(pair.getFirst());
                if (classInfo == null) {
                    Skript.error("Unknown type '" + matcher.group(2) + "'");
                    return false;
                }

                final Argument<?> arg = Argument.newInstance(matcher.group(1), classInfo, matcher.group(3), i, !pair.getSecond(), optionals > 0);
                if (arg == null)
                    return false;
                currentArguments.add(arg);
                if (arg.isOptional() && optionals == 0) {
                    pattern.append('[');
                    optionals++;
                }
                pattern.append("%").append(arg.isOptional() ? "-" : "").append(Utils.toEnglishPlural(classInfo.getCodeName(), pair.getSecond())).append("%");
                if (arg.isOptional() && optionals >= 1) {
                    pattern.append("]");
                    optionals--;
                }
            }
        }

        SectionNode triggerNode = (SectionNode) node.get("trigger");
        if (triggerNode == null) return false;
        trigger = new Trigger(node.getConfig().getFile(),
                "discord command " + commandName,
                this,
                ScriptLoader.loadItems(triggerNode));
        trigger.setLineNumber(node.getLine());

        String registered = CommandManager.get().isRegistered(commandName);

        if (registered != null) {
            Skript.error("Discord command " + commandName + " is already registered! (" + registered + ")");
            return false;
        }
        CommandManager.get().addCommand(node.getConfig().getFile(), commandName);

        // We do this to prevent a java.util.ConcurrentModificationException
        List<Node> tempNodes = new ArrayList<>();
        for (Node subNode : node) {
            tempNodes.add(subNode);
        }
        for (Node subNode : tempNodes) {
            node.remove(subNode);
        }
        ParserInstance.get().setCurrentEvent("discord command", BukkitDiscordCommandEvent.class);
        return true;
    }

    @Override
    public boolean check(Event e) {
        BukkitDiscordCommandEvent event = (BukkitDiscordCommandEvent) e;
        DiscordCommand command = event.getCommand();
        if (commandMatches(command.getUsedAlias())) {
            // TODO check if arguments match and shit blah blah idk how imma do that </3
            trigger.execute(event);
            return true;
        }
        return false;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "discord command " + commandName + (unparsedArguments == null ? "" : " " + unparsedArguments);
    }

    private boolean commandMatches(String alias) {
        for (String tempAlias : aliases) {
            for (String prefix : prefixes) {
                if ((prefix + tempAlias).equalsIgnoreCase(alias)) {
                    return true;
                }
            }
        }
        return false;
    }
}
