package com.ankoki.skriptdiscord.skript.commands;

import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.MessageChannel;

@RequiredArgsConstructor
public class DiscordCommand {

    @Getter
    private final Object executor;
    @Getter
    private final DiscordBot bot;
    @Getter
    private final MessageChannel channel;
    @Getter
    private final boolean inGuild;
    @Getter
    private final String fullCommand;
    @Getter
    private final String usedAlias;
    @Getter
    private final String[] unparsedArguments;
}
