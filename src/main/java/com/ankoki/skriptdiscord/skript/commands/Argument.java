package com.ankoki.skriptdiscord.skript.commands;

import java.util.WeakHashMap;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.log.RetainingLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Utils;
import ch.njol.skript.variables.Variables;

/**
 * Represents an argument of a command
 * <p>
 * We copy this into skript-discord because the
 * default Argument class only accepts ScriptCommandEvent
 *
 * @author Peter Güttinger
 */
@RequiredArgsConstructor
public class Argument<T> {

    private final String name;
    private final ClassInfo<T> type;
    private final Expression<? extends T> def;
    private final boolean single;
    private final int index;
    private final boolean optional;

    // Not final purely to benefit from RequiredArgsConstructor
    private transient WeakHashMap<Event, T[]> current = new WeakHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> Argument<T> newInstance(String name, ClassInfo<T> type, String def, int index, boolean single, boolean forceOptional) {
        if (name != null && !Variable.isValidVariableName(name, false, false)) {
            Skript.error("An argument's name must be a valid variable name, and cannot be a list variable.");
            return null;
        }
        Expression<? extends T> expr = null;
        if (def != null) {
            if (def.startsWith("%") && def.endsWith("%")) {
                RetainingLogHandler log = SkriptLogger.startRetainingLog();
                try {
                    expr = new SkriptParser(def.substring(1, def.length() - 1), SkriptParser.PARSE_EXPRESSIONS, ParseContext.COMMAND)
                            .parseExpression(type.getC());
                    if (expr == null) {
                        log.printErrors("Can't understand this expression: " + def + "");
                        return null;
                    }
                    log.printLog();
                } finally {
                    log.stop();
                }
            } else {
                RetainingLogHandler log = SkriptLogger.startRetainingLog();
                try {
                    if (type.getC() == String.class) {
                        if (def.startsWith("\"") && def.endsWith("\""))
                            expr = (Expression<? extends T>) VariableString.newInstance("" + def.substring(1, def.length() - 1));
                        else
                            expr = (Expression<? extends T>) new SimpleLiteral<>(def, false);
                    } else {
                        expr = new SkriptParser(def, SkriptParser.PARSE_LITERALS, ParseContext.DEFAULT).parseExpression(type.getC());
                    }
                    if (expr == null) {
                        log.printErrors("Can't understand this expression: '" + def + "'");
                        return null;
                    }
                    log.printLog();
                } finally {
                    log.stop();
                }
            }
        }
        return new Argument<T>(name, type, expr, single, index, def != null || forceOptional);
    }

    @Override
    public String toString() {
        final Expression<? extends T> def = this.def;
        return "<" + (name != null ? name + ": " : "") + Utils.toEnglishPlural(type.getCodeName(), !single) + (def == null ? "" : " = " + def) + ">";
    }

    public boolean isOptional() {
        return optional;
    }

    public void setToDefault(Event event) {
        if (def != null)
            set(event, def.getArray(event));
    }

    @SuppressWarnings("unchecked")
    public void set(Event event, Object[] objects) {
        if (!type.getC().isAssignableFrom(objects.getClass().getComponentType()))
            throw new IllegalArgumentException();
        current.put(event, (T[]) objects);
        String name = this.name;
        if (name != null) {
            if (single) {
                if (objects.length > 0)
                    Variables.setVariable(name, objects[0], event, true);
            } else {
                for (int i = 0; i < objects.length; i++)
                    Variables.setVariable(name + "::" + (i + 1), objects[i], event, true);
            }
        }
    }

    public T[] getCurrent(Event event) {
        return current.get(event);
    }

    public Class<T> getType() {
        return type.getC();
    }

    public int getIndex() {
        return index;
    }

    public boolean isSingle() {
        return single;
    }
}