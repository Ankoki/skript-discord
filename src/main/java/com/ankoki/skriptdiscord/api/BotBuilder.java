package com.ankoki.skriptdiscord.api;

import ch.njol.skript.Skript;
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

    private final List<GatewayIntent> intents = new ArrayList<>();
    private String name;
    private String description = "<none>";
    private String token;
    private Activity activity;
    private boolean init;

    private JDA jda;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public JDA getJda() {
        return jda;
    }

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
                        .addEventListeners() // TODO add listeners when applicable.
                        .setActivity(activity)
                        .build();
            } catch (LoginException | InterruptedException ex) {
                ex.printStackTrace();
                Skript.error("skript-discord | Something went horribly wrong when logging into the bot! Is the token correct?");
                return false;
            }
            init = true;
            return true;
        });
        while (!future.isDone()){}
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @SneakyThrows
    public DiscordBot build() {
        if (!init) throw new IllegalAccessException("You need to call init() first!");
        return new DiscordBot(name, description, jda);
    }
}
