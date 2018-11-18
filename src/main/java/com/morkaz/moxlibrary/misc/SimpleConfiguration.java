package com.morkaz.moxlibrary.misc;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleConfiguration {

	private Plugin plugin;
	private FileConfiguration sourceConfig;
	private File configFile;

	public SimpleConfiguration(Plugin plugin) {
		this.plugin = plugin;
		sourceConfig = new YamlConfiguration();
		this.reloadConfig();
		try {
			sourceConfig.load(new InputStreamReader(plugin.getResource("config.yml")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void reloadConfig(){
		this.processConfigFile();
	}

	public FileConfiguration getConfig(){
		return plugin.getConfig();
	}

	private void processConfigFile() {
		configFile = new File(plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.saveResource("config.yml", false);
		}
		plugin.reloadConfig();
	}

}
