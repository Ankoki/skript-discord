package com.ankoki.skriptdiscord.api.bot;

import ch.njol.skript.Skript;
import com.ankoki.skriptdiscord.discord.DiscordListener;
import com.ankoki.skriptdiscord.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BotBuilder {

    @Getter
    private final List<GatewayIntent> intents = new ArrayList<>();
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description = "<none>";
    @Getter
    @Setter
    private String token;
    @Getter
    @Setter
    private Activity activity;

    private boolean init;
    @Getter
    private JDA jda;

    public void allowIntent(GatewayIntent... intents) {
        Collections.addAll(this.intents, intents);
    }

    public boolean init() {
        if (token == null || name == null) {
            Skript.error("skript-discord | You cannot create a bot without a name or token!");
            return false;
        }
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            try {
                jda = JDABuilder.createDefault(token).build();
                jda.awaitReady();
                JDABuilder.createLight(token, intents)
                        .addEventListeners(new DiscordListener())
                        .setActivity(activity)
                        .build();
            } catch (LoginException | InterruptedException ex) {
                Utils.throwException(ex);
                return false;
            }
            init = true;
            return true;
        });
        while (!future.isDone()){}
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException ex) {
            Utils.throwException(ex);
            return false;
        }
    }

    @SneakyThrows
    public DiscordBot build() {
        if (!init) throw new IllegalAccessException("You need to call init() first!");
        return new DiscordBot(name, description, jda);
    }
}
