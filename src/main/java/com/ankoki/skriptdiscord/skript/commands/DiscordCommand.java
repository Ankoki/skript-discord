package com.ankoki.skriptdiscord.skript.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;

@RequiredArgsConstructor
public class DiscordCommand {

    @Getter
    private final Member member;
    @Getter
    private final String prefix;
    @Getter
    private final String fullCommand;
    @Getter
    private final String usedAlias;
    @Getter
    private final String[] arguments;
}
