package com.ankoki.skriptdiscord.discord;

import com.ankoki.skriptdiscord.api.bot.BotManager;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
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
        DiscordBot bot = BotManager.getBot(event.getJDA());
        if (bot != null && bot.getPrefix() != null) {
            String message = event.getMessage().getContentRaw();
            String alias = Utils.getCommandName(message, bot.getPrefix());
            DiscordCommand command = new DiscordCommand(event.getMember(), bot.getPrefix(), message, alias, Utils.getCommandArguments(message));
            Bukkit.getPluginManager().callEvent(new BukkitDiscordCommandEvent(command));
        }
        // TODO fire `on discord message` and `on discord guild message`
    }
}
