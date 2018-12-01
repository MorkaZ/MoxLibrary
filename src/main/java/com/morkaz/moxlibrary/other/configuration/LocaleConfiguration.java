package com.morkaz.moxlibrary.other.configuration;

import com.morkaz.moxlibrary.api.ConfigUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LocaleConfiguration extends SimpleConfiguration {

	private File messagesFile;
	private Plugin plugin;
	private String defaultLocale, configLocaleLocation, localeConfigName;
	private YamlConfiguration messagesConfig, sourceMessagesConfig;

	public LocaleConfiguration(Plugin plugin, String localeConfigName, String defaultLocaleShort, String configLocaleLocation) {
		super(plugin);
		this.plugin = plugin;
		this.localeConfigName = localeConfigName;
		this.defaultLocale = defaultLocaleShort;
		this.configLocaleLocation = configLocaleLocation;
		//Load default source messages configuration
		this.sourceMessagesConfig = new YamlConfiguration();
		try {
			this.sourceMessagesConfig.load(new InputStreamReader(plugin.getResource(this.localeConfigName+"-"+defaultLocaleShort+".yml")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		//Load messages and check for missing indexes
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

	public void reloadConfiguration(){
		super.reloadConfiguration();
		this.processMessagesFile();
	}

	private void processMessagesFile() {
		String fileName = this.localeConfigName+"-"+this.defaultLocale+".yml";
		String locale = (getConfig().getString(this.configLocaleLocation)+"").toLowerCase();
		if (locale.equals("") || locale.equals("null")){
			locale = this.defaultLocale;
		}
		if (plugin.getResource(this.localeConfigName+"-"+locale+".yml") != null) {
			fileName = this.localeConfigName+"-"+locale+".yml";
			messagesFile = new File(plugin.getDataFolder(), fileName);
			if (!messagesFile.exists()) {
				messagesFile.getParentFile().mkdirs();
				plugin.saveResource(fileName, false);
			}
		} else {
			InputStream inputStream = plugin.getResource(fileName);
			messagesFile = new File(plugin.getDataFolder(), this.localeConfigName+"-"+locale+".yml");
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
							"Error happened when trying to create new messages file! Default \""+fileName+"\" has been saved! Error stacktrace above!");
					messagesFile.getParentFile().mkdirs();
					plugin.saveResource(fileName, false);
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
		ConfigUtils.setMissingValues(sourceMessagesConfig, messagesConfig, messagesFile);
	}

}
