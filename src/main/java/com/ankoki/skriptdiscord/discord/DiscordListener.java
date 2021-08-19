package com.ankoki.skriptdiscord.discord;

import com.ankoki.skriptdiscord.skript.commands.BukkitDiscordCommandEvent;
import com.ankoki.skriptdiscord.skript.commands.DiscordCommand;
import com.ankoki.skriptdiscord.utils.Utils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();
        if (!message.isEmpty()) {
            DiscordCommand command = new DiscordCommand(event.getMember(), event.getChannel(), message, (message.split(" ")[0]), Utils.getCommandArguments(message));
            Utils.runSync(() -> Bukkit.getPluginManager().callEvent(new BukkitDiscordCommandEvent(command)));
        }
        // TODO fire `on discord message` and `on discord guild message`
    }
}
