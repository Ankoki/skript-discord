package com.ankoki.skriptdiscord.skript.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

@RequiredArgsConstructor
public class DiscordCommand {

    @Getter
    private final Member member;
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
