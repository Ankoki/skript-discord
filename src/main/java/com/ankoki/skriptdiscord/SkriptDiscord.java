package com.ankoki.skriptdiscord;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;
import com.ankoki.skriptdiscord.api.bot.DiscordBot;
import com.ankoki.skriptdiscord.api.DiscordMessage;
import com.ankoki.skriptdiscord.api.bot.BotManager;
import com.ankoki.skriptdiscord.utils.Console;
import com.ankoki.skriptdiscord.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.text.DecimalFormat;

// TODO [01:19:25 WARN]: Exception in thread "JDA Shutdown Hook" java.lang.NoClassDefFoundError: com/ankoki/skriptdiscord/libs/jda/internal/managers/AudioManagerImpl
// Include audio shit
public class SkriptDiscord extends JavaPlugin {

    private static SkriptDiscord instance;
    private SkriptAddon addon;
    private static final DecimalFormat sf = new DecimalFormat("0.00");

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        addon = Skript.registerAddon(this);
        registerClassInfo();
        try {
            addon.loadClasses("com.ankoki.skriptdiscord.skript");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long fin = System.currentTimeMillis() - start;
        Console.log("Successfully enabled in " + sf.format(fin) + " seconds (" + fin + "ms)");
    }

    @Override
    public void onDisable() {
        instance = null;
        addon = null;
        BotManager.disableAll();
    }

    public static SkriptDiscord getInstance() {
        return instance;
    }

    private void registerClassInfo() {
        Classes.registerClass(new ClassInfo<>(DiscordMessage.class, "discordmessage")
                .user("discordmessage?s?")
                .name("Discord Message")
                .description("A DiscordMessage object.")
                .since("1.0"));

        Classes.registerClass(new ClassInfo<>(DiscordBot.class, "discordbot")
                .user("discordbot?s?")
                .name("Discord Bot")
                .description("A DiscordBot object.")
                .since("1.0"));

        Classes.registerClass(new ClassInfo<>(User.class, "discorduser")
                .user("discorduser?s?")
                .name("Discord User")
                .description("A User object.")
                .since("1.0"));

        Classes.registerClass(new ClassInfo<>(Member.class, "discordmember")
                .user("discordmember?s?")
                .name("Discord Member")
                .description("A Member object.")
                .since("1.0"));

        Classes.registerClass(new ClassInfo<>(Guild.class, "discordguild")
                .user("discordguild?s?")
                .name("Discord Guild")
                .description("A Guild object.")
                .since("1.0"));

        Classes.registerClass(new ClassInfo<>(MessageChannel.class, "discordchannel")
                .user("discordchannel?s?")
                .name("Discord Channel")
                .description("A MessageChannel object.")
                .since("1.0"));

        Classes.registerClass(new ClassInfo<>(GatewayIntent.class, "discordintent")
                .user("discordintent?s?")
                .name("Discord Intent")
                .description("A Discord Gateway Intent")
                .since("1.0")
                .parser(new Parser<GatewayIntent>() {
                    @Override
                    public GatewayIntent parse(String s, ParseContext context) {
                        return Utils.getGatewaySafely(s.replace(" ", "_").toUpperCase());
                    }

                    @Override
                    public String toString(GatewayIntent o, int flags) {
                        return o.toString().toLowerCase().replace("_", " ");
                    }

                    @Override
                    public String toVariableNameString(GatewayIntent o) {
                        return o.toString().toLowerCase().replace("_", " ");
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return "[a-z ]+";
                    }
                }));

        Converters.registerConverter(DiscordMessage.class, String.class, DiscordMessage::getMessage);
        // Below will not work until issue #4272 is addressed.
        Converters.registerConverter(String.class, DiscordMessage.class, DiscordMessage::new);
        Converters.registerConverter(DiscordBot.class, String.class, DiscordBot::toString);
        Converters.registerConverter(Member.class, User.class, Member::getUser);
        Converters.registerConverter(Member.class, String.class, Member::getEffectiveName);
        Converters.registerConverter(User.class, String.class, User::getName);
    }
}
