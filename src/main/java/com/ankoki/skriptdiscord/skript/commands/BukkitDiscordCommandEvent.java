package com.ankoki.skriptdiscord.skript.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BukkitDiscordCommandEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final DiscordCommand command;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
