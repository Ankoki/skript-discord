package com.ankoki.skriptdiscord.skript.commands;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
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
import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.Utils;
import ch.njol.util.NonNullPair;
import ch.njol.util.StringUtils;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("Discord Command")
@Description("Register a discord command with Skript.")
@Examples({"discord command repeat <text>:",
        "\tprefixes: . and !",
        "\taliases: echo",
        "\tdescription: Repeats the text that was sent.",
        "\ttrigger:",
        "\t\tsend arg-1 to event-channel"})
@Since("1.0")
public class EvtDiscordCommand extends SkriptEvent {

    static {
        Skript.registerEvent("Discord Command", EvtDiscordCommand.class, BukkitDiscordCommandEvent.class,
                "discord command <([^\\s]+)( .+)?$>");

        EventValues.registerEventValue(BukkitDiscordCommandEvent.class, Member.class, new Getter<Member, BukkitDiscordCommandEvent>() {
            @Override
            public Member get(BukkitDiscordCommandEvent event) {
                Object obj = event.getCommand().getExecutor();
                if (obj instanceof Member) return ((Member) obj);
                return null;
            }
        }, 0);

        EventValues.registerEventValue(BukkitDiscordCommandEvent.class, User.class, new Getter<User, BukkitDiscordCommandEvent>() {
            @Override
            public User get(BukkitDiscordCommandEvent event) {
                Object obj = event.getCommand().getExecutor();
                if (obj instanceof Member) return ((Member) obj).getUser();
                return ((User) event.getCommand().getExecutor());
            }
        }, 0);

        EventValues.registerEventValue(BukkitDiscordCommandEvent.class, MessageChannel.class, new Getter<MessageChannel, BukkitDiscordCommandEvent>() {
            @Override
            public MessageChannel get(BukkitDiscordCommandEvent event) {
                return event.getCommand().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(BukkitDiscordCommandEvent.class, DiscordBot.class, new Getter<DiscordBot, BukkitDiscordCommandEvent>() {
            @Override
            public DiscordBot get(BukkitDiscordCommandEvent event) {
                return event.getCommand().getBot();
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
            .addEntry("cooldown", true)
            .addEntry("cooldown message", true)
            .addEntry("prefixes", false)
            .addSection("trigger", false);
    public static List<Argument<?>> lastArguments = new ArrayList<>();
    private static final Map<String, Long> commandCooldowns = new HashMap<>();

    private String commandName;
    private String unparsedArguments;
    private final List<Argument<?>> currentArguments = new ArrayList<>();

    private final List<String> aliases = new ArrayList<>();
    private String description;
    private final List<String> executableIn = new ArrayList<>();
    private final List<String> roles = new ArrayList<>();
    private final List<String> bots = new ArrayList<>();
    private final List<Permission> permissions = new ArrayList<>();
    private String permissionMessage;
    private Expression<Timespan> cooldownExpr;
    private String cooldownMessage;
    private final List<String> prefixes = new ArrayList<>();
    private Trigger trigger;
    private String argumentSkriptPattern;

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

        String rawAliases = ScriptLoader.replaceOptions(node.get("aliases", ""));
        if (!rawAliases.equals("")) {
            Collections.addAll(aliases, rawAliases.split(listPattern));
        }
        aliases.add(commandName);

        description = ScriptLoader.replaceOptions(node.get("description", ""));

        String rawRoles = ScriptLoader.replaceOptions(node.get("roles", ""));
        Collections.addAll(roles, rawRoles.split(listPattern));

        String rawBots = ScriptLoader.replaceOptions(node.get("bots", ""));
        Collections.addAll(bots, rawBots.split(listPattern));

        String rawExecutable = ScriptLoader.replaceOptions(node.get("executable in", "guild and dm"));
        Collections.addAll(executableIn, rawExecutable.split(listPattern));

        String rawPermissions = ScriptLoader.replaceOptions(node.get("permission", ""));
        for (String perm : rawPermissions.split(listPattern)) {
            try {
                Permission permission = Permission.valueOf(perm.toUpperCase().replace(" ", "_"));
                permissions.add(permission);
            } catch (IllegalArgumentException ex) {
                Skript.error("'" + perm + "' is not a valid permission!");
                return false;
            }
        }

        permissionMessage = ScriptLoader.replaceOptions(node.get("permission message", ""));

        String rawCooldown = ScriptLoader.replaceOptions(node.get("cooldown", ""));
        ParseResult result = SkriptParser.parse(rawCooldown, "%timespan%");
        if (result == null) cooldownExpr = null;
        else {
            if (result.exprs.length < 1) cooldownExpr = null;
            else cooldownExpr = (Expression<Timespan>) result.exprs[0];
        }

        cooldownMessage = ScriptLoader.replaceOptions(node.get("cooldown message", ""));

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
                classInfo = Classes.getClassInfoFromUserInput(matcher.group(2));
                NonNullPair<String, Boolean> pair = Utils.getEnglishPlural(matcher.group(2));
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
            argumentSkriptPattern = pattern.toString();
        }

        ParserInstance.get().setCurrentEvent("discord command", BukkitDiscordCommandEvent.class);
        lastArguments = currentArguments;

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

        return true;
    }

    @Override
    public boolean check(Event e) {
        BukkitDiscordCommandEvent event = (BukkitDiscordCommandEvent) e;
        DiscordCommand command = event.getCommand();
        if (commandMatches(command.getUsedAlias())) {
            Object obj = command.getExecutor();
            if (!bots.isEmpty() && !bots.contains(command.getBot().getName())) return false;
            if (!command.isInGuild() && !executableIn.contains("guild")) return false;
            Member member = null;
            if (obj instanceof Member) member = (Member) obj;
            if (member != null) {
                Guild guild = member.getGuild();
                boolean hasRole = false;
                for (String roleName : roles) {
                    List<Role> guildRolesByName = guild.getRolesByName(roleName, false);
                    for (Role role : guildRolesByName) {
                        if (member.getRoles().contains(role)) {
                            hasRole = true;
                            break;
                        }
                    }
                    if (hasRole) break;
                }
                if (!hasRole) return false;
                if (member.hasPermission(permissions)) {
                    if (!permissionMessage.isEmpty()) {
                        command.getChannel().sendMessage(permissionMessage).queue();
                    }
                    return false;
                }
            }
            if (cooldownExpr != null) {
                Timespan cooldown = cooldownExpr.getSingle(event);
                if (cooldown != null) {
                    long millis = cooldown.getMilliSeconds();
                    if (commandCooldowns.containsKey(commandName)) {
                        long lastExecuted = commandCooldowns.get(commandName);
                        if (System.currentTimeMillis() - millis < lastExecuted) {
                            if (!cooldownMessage.isEmpty()) command.getChannel().sendMessage(cooldownMessage).queue();
                            return false;
                        }
                    }
                    commandCooldowns.put(commandName, System.currentTimeMillis());
                }
            }
            ParseResult result = SkriptParser.parse(String.join(" ", command.getUnparsedArguments()), argumentSkriptPattern);
            if (result == null) return false;
            Expression<?>[] exprs = result.exprs;
            if (exprs.length != currentArguments.size()) return false;
            int i = 0;
            for (Argument<?> argument : currentArguments) {
                if (exprs[i] == null) {
                    argument.setToDefault(event);
                } else {
                    argument.set(event, exprs[i].getArray(event));
                }
                i++;
            }
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
