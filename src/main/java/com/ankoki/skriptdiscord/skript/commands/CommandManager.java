package com.ankoki.skriptdiscord.skript.commands;

import ch.njol.skript.config.Config;
import ch.njol.skript.events.bukkit.PreScriptLoadEvent;
import com.ankoki.skriptdiscord.SkriptDiscord;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager implements Listener {

    static {
        Bukkit.getPluginManager().registerEvents(CommandManager.get(), SkriptDiscord.getInstance());
    }

    private static CommandManager singleton;

    public static CommandManager get() {
        singleton = singleton == null ? new CommandManager() : singleton;
        return singleton;
    }

    private final Map<File, List<String>> REGISTERED_COMMANDS = new ConcurrentHashMap<>();

    public void clearFile(File file) {
        REGISTERED_COMMANDS.remove(file);
    }

    public void addCommand(File file, String command) {
        List<String> list = REGISTERED_COMMANDS.getOrDefault(file, new ArrayList<>());
        list.add(command);
        REGISTERED_COMMANDS.put(file, list);
    }

    public String isRegistered(String string) {
        for (Map.Entry<File, List<String>> entry : REGISTERED_COMMANDS.entrySet()) {
            if (entry.getValue().contains(string)) {
                return entry.getKey().getName();
            }
        }
        return null;
    }

    @EventHandler
    private void onScriptLoad(PreScriptLoadEvent event) {
        for (Config config : event.getScripts()) {
            clearFile(config.getFile());
        }
    }
}
