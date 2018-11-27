package com.morkaz.moxlibrary.misc.configuration;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LocaleConfiguration extends SimpleConfiguration {

	private File messagesFile;
	private Plugin plugin;
	private String defaultLocale, configLocaleLocation;
	private YamlConfiguration messagesConfig;

	public LocaleConfiguration(Plugin plugin, String defaultLocaleShort, String configLocaleLocation) {
		super(plugin);
		this.plugin = plugin;
		this.defaultLocale = defaultLocaleShort;
		this.configLocaleLocation = configLocaleLocation;
		this.reloadMessagesOnly();
	}

	public FileConfiguration getMessagesConfig(){
		return this.messagesConfig;
	}

	public void reloadConfigOnly(){
		super.reloadConfiguration();
	}

	public void reloadMessagesOnly(){
		this.processMessagesFile();
	}

	@Override
	public void reloadConfiguration(){
		super.reloadConfiguration();
		this.processMessagesFile();
	}


	private void processMessagesFile() {
		String filename = "messages-"+defaultLocale+".yml";
		if (plugin.getResource("messages-"+getConfig().getString(configLocaleLocation).toLowerCase()+".yml") != null) {
			filename = "messages-"+getConfig().getString(configLocaleLocation).toLowerCase()+".yml";
			messagesFile = new File(plugin.getDataFolder(), filename);
			if (!messagesFile.exists()) {
				messagesFile.getParentFile().mkdirs();
				plugin.saveResource(filename, false);
			}
		} else {
			InputStream inputStream = plugin.getResource(filename);
			messagesFile = new File(plugin.getDataFolder(), "messages-"+getConfig().getString(configLocaleLocation).toLowerCase()+".yml");
			if (!messagesFile.exists()) {
				messagesFile.getParentFile().mkdirs();
				try {
					messagesFile.createNewFile();
					FileUtils.copyInputStreamToFile(inputStream, messagesFile);
					Bukkit.getLogger().warning("["+plugin.getDescription().getName()+"] " +
							"Your locale is not supported at this moment in this plugin. " +
							"New messages file has been generated, but you must translate it to your language from default locale.");
				} catch (IOException e) {
					e.printStackTrace();
					Bukkit.getLogger().warning("["+plugin.getDescription().getName()+"] " +
							"Error happened when trying to create new messages file! Default messages-"+defaultLocale+".yml has been saved! Error stacktrace above!");
					messagesFile.getParentFile().mkdirs();
					plugin.saveResource(filename, false);
				}
			}
		}
		messagesConfig = new YamlConfiguration();
		try {
			messagesConfig.load(messagesFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}
