package com.ankoki.skriptdiscord.discord.events;

import com.ankoki.skriptdiscord.discord.DiscordBot;
import com.ankoki.skriptdiscord.discord.events.bukkit.commands.MessageCommandEvent;
import com.ankoki.skriptdiscord.discord.events.bukkit.messages.*;
import com.ankoki.skriptdiscord.discord.events.bukkit.messages.guild.GuildMessageEvent;
import com.ankoki.skriptdiscord.discord.events.bukkit.messages.guild.NewsMessageEvent;
import com.ankoki.skriptdiscord.discord.events.bukkit.messages.guild.ThreadMessageEvent;
import com.ankoki.skriptdiscord.discord.events.bukkit.messages.guild.VoiceMessageEvent;
import com.ankoki.skriptdiscord.handlers.ChatCommands;
import com.ankoki.skriptdiscord.misc.Misc;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

public class DiscordAdapter extends ListenerAdapter {

	private final DiscordBot bot;

	/**
	 * Creates a new discord event adapter which listens to events.
	 *
	 * @param bot the bot which will listen to these events.
	 */
	public DiscordAdapter(DiscordBot bot) {
		this.bot = bot;
	}

	/**
	 * Gets the bot associated with these events.
	 *
	 * @return the bot.
	 */
	public DiscordBot getBot() {
		return bot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		User user = event.getAuthor();
		Member member = event.getMember();
		Message message = event.getMessage();
		MessageChannel channel = event.getChannel();
		ChatCommands.Command command = Misc.isCommand(message);
		if (command != null) {
			MessageCommandEvent commandEvent = new MessageCommandEvent(command.getBot(), user, channel, Misc.getArguments(message));
			Bukkit.getPluginManager().callEvent(commandEvent);
			if (!commandEvent.isCancelled())
				command.execute(commandEvent, commandEvent.getArguments());
			return;
		}
		MessageEvent messageEvent;
		if (channel instanceof PrivateChannel privateChannel)
			messageEvent = new PrivateMessageEvent(bot, user, message, privateChannel);
		else if (channel instanceof NewsChannel newsChannel)
			messageEvent = new NewsMessageEvent(bot, member, message, newsChannel);
		else if (channel instanceof VoiceChannel voiceChannel)
			messageEvent = new VoiceMessageEvent(bot, member, message, voiceChannel);
		else if (channel instanceof ThreadChannel threadChannel)
			messageEvent = new ThreadMessageEvent(bot, member, message, threadChannel);
		else if (channel instanceof GuildMessageChannel guildChannel)
			messageEvent = new GuildMessageEvent(bot, member, message, guildChannel);
		else
			throw new IllegalStateException("MessageReceivedEvent called where the channel was of an unexpected channel " +
					"type. Unsupported class name: " + channel.getClass().getName());
		Bukkit.getPluginManager().callEvent(messageEvent);
		if (messageEvent.isCancelled())
			message.delete().queue();
	}
}
