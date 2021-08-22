package com.ankoki.skriptdiscord.skript.commands.values;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.command.ScriptCommandEvent;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Utils;
import ch.njol.util.Kleenean;
import ch.njol.util.StringUtils;
import com.ankoki.skriptdiscord.skript.commands.Argument;
import com.ankoki.skriptdiscord.skript.commands.BukkitDiscordCommandEvent;
import com.ankoki.skriptdiscord.skript.commands.EvtDiscordCommand;
import org.bukkit.event.Event;

import java.util.List;
import java.util.regex.MatchResult;

/**
 * This class is an exact replica of {@link ch.njol.skript.expressions.ExprArgument}
 * <p>
 * The reason we have to have two is due to this accepting a different event.
 */
public class ExprArgument extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprArgument.class, Object.class, ExpressionType.SIMPLE,
                "[the] last arg[ument][s]",
                "[the] arg[ument][s](-| )<(\\d+)>",
                "[the] <(\\d*1)st|(\\d*2)nd|(\\d*3)rd|(\\d*[4-90])th> arg[ument][s]",
                "[the] arg[ument][s]",
                "[the] %*classinfo%( |-)arg[ument][( |-)<\\d+>]",
                "[the] arg[ument]( |-)%*classinfo%[( |-)<\\d+>]");
    }

    private Argument<?> argument;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        List<Argument<?>> currentArguments = EvtDiscordCommand.lastArguments;
        if (currentArguments == null || (!getParser().isCurrentEvent(BukkitDiscordCommandEvent.class) && !getParser().isCurrentEvent(ScriptCommandEvent.class))) {
            Skript.error("The expression 'argument' can only be used within a command");
            return false;
        } else if (currentArguments.size() == 0) {
            Skript.error("This command doesn't have any arguments");
            return false;
        }
        Argument<?> arg = null;
        switch (matchedPattern) {
            case 0:
                arg = currentArguments.get(currentArguments.size() - 1);
                break;
            case 1:
            case 2:
                // Figure out in which format (1st, 2nd, 3rd, Nth) argument was given in
                MatchResult regex = parseResult.regexes.get(0);
                String argMatch = null;
                for (int i = 1; i <= 4; i++) {
                    argMatch = regex.group(i);
                    if (argMatch != null) {
                        break; // Found format
                    }
                }
                assert argMatch != null;
                int i = Utils.parseInt(argMatch);

                if (i > currentArguments.size()) {
                    Skript.error("The command doesn't have a " + StringUtils.fancyOrderNumber(i) + " argument");
                    return false;
                } else if (i < 1) {
                    Skript.error("Command arguments start from one; argument number " + i + " is invalid");
                    return false;
                }
                arg = currentArguments.get(i - 1);
                break;
            case 3:
                if (currentArguments.size() == 1) {
                    arg = currentArguments.get(0);
                } else {
                    Skript.error("'argument(s)' cannot be used if the command has multiple arguments. Use 'argument 1', 'argument 2', etc. instead");
                    return false;
                }
                break;
            case 4:
            case 5:
                @SuppressWarnings("unchecked")
                ClassInfo<?> c = ((Literal<ClassInfo<?>>) exprs[0]).getSingle();
                @SuppressWarnings("null")
                final int num = parseResult.regexes.size() > 0 ? Utils.parseInt(parseResult.regexes.get(0).group()) : -1;
                int j = 1;
                for (final Argument<?> a : currentArguments) {
                    if (!c.getC().isAssignableFrom(a.getType()))
                        continue;
                    if (arg != null) {
                        Skript.error("There are multiple " + c + " arguments in this command");
                        return false;
                    }
                    if (j < num) {
                        j++;
                        continue;
                    }
                    arg = a;
                    if (j == num)
                        break;
                }
                if (arg == null) {
                    j--;
                    if (num == -1 || j == 0)
                        Skript.error("There is no " + c + " argument in this command");
                    else if (j == 1)
                        Skript.error("There is only one " + c + " argument in this command");
                    else
                        Skript.error("There are only " + j + " " + c + " arguments in this command");
                    return false;
                }
                break;
            default:
                return false;
        }
        this.argument = arg;
        return true;
    }

    @Override
    protected Object[] get(Event event) {
        return argument.getCurrent(event);
    }

    @Override
    public boolean isSingle() {
        return argument.isSingle();
    }

    @Override
    public Class<?> getReturnType() {
        return argument.getType();
    }

    @Override
    public String toString(Event e, boolean debug) {
        if (e == null)
            return "the " + StringUtils.fancyOrderNumber(argument.getIndex() + 1) + " argument";
        return Classes.getDebugMessage(getArray(e));
    }
}
