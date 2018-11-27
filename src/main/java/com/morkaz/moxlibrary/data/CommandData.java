package com.morkaz.moxlibrary.data;

import org.bukkit.command.CommandExecutor;

import java.util.List;
import org.bukkit.plugin.Plugin;

public class CommandData {

	private String description, mainCommand, usage;
	private List<String> aliases;
	private CommandExecutor commandExecutor;
	private Plugin plugin;


	public CommandData(Plugin plugin, String description, String mainCommand, String usage, List<String> aliases, CommandExecutor commandExecutor) {
		this.description = description;
		this.mainCommand = mainCommand;
		this.usage = usage;
		this.aliases = aliases;
		this.commandExecutor = commandExecutor;
		this.plugin = plugin;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public String getDescription() {
		return description;
	}

	public String getMainCommand() {
		return mainCommand;
	}

	public String getUsage() {
		return usage;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
}
