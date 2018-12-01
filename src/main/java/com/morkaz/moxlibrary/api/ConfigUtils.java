package com.morkaz.moxlibrary.api;

import com.morkaz.moxlibrary.data.SoundData;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConfigUtils {


	/**
	 * This method will check for keys that are not existing comparing to configuration file in your plugin's .jar.
	 * It will not reset old keys. It will just add that ones that are not existing with default content.
	 *
	 * You can easy load source files doing something like this:
	 * {@code InputStream input = plugin.getResource("config.yml");
	 * 	FileConfiguration sourceConfig;
	 * 	sourceConfig.load(new InputStreamReader(input));}
	 *
	 * 	And then you can check for missing values like this:
	 * 	{@code
	 *   ConfigUtils.missingValuesSet(sourceConfig, plugin.getConfig(), new File(main.getDataFolder(), "config.yml")); 
	 *   plugin.reloadConfig();}
	 *
	 * @param sourceConfiguration FileConfiguration read from your .jar that is having actual keys with defaults.
	 * @param targetConfiguration FileConfiguration that already exists as an file in your plugin data folder.
	 * @param targetFile File with correct path to configuration that will be checked for having all actual configuration keys.
	 *                If not, this file will be updated with new keys without reseting old ones.
	 * @return - Updated configuration with actual keys.
	 */
	public static FileConfiguration setMissingValues(FileConfiguration sourceConfiguration, FileConfiguration targetConfiguration, File targetFile) {
		Boolean found = false;
		for (String key : sourceConfiguration.getKeys(true)) {
			if (!targetConfiguration.isSet(key)) {
				targetConfiguration.set(key, sourceConfiguration.get(key));
				found = true;
			}
		}
		if (targetFile != null) {
			if (found) {
				try {
					targetConfiguration.save(targetFile);
				} catch (IOException e) {
					e.printStackTrace();
					Bukkit.getLogger().warning("[MoxLibrary] Can not update values in configuration file: "+targetFile.getAbsolutePath());
				}
			}
		}
		return targetConfiguration;
	}

	public static FileConfiguration loadFileConfiguration(Plugin plugin, String configFileName, Boolean setMissingKeysWithDefaultValues){
		FileConfiguration config = new YamlConfiguration();
		String fileName = configFileName+(configFileName.contains(".yml") ? "" : ".yml");
		File configFile = new File(plugin.getDataFolder(), fileName);
		Boolean bool = false;
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.saveResource(fileName, false);
			bool = true;
		}
		try {
			config.load(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		if (setMissingKeysWithDefaultValues){
			setMissingValues(loadSourceConfiguration(plugin, configFileName), config, configFile);
		}
		return config;
	}

	public static FileConfiguration loadSourceConfiguration(Plugin plugin, String configFileName){
		FileConfiguration sourceConfig = new YamlConfiguration();
		try {
			sourceConfig.load(new InputStreamReader(plugin.getResource(configFileName+(configFileName.contains(".yml") ? "" : ".yml"))));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return sourceConfig;
	}

	@Nullable
	public static SoundData loadSoundData(FileConfiguration config, String contentPrefix, Plugin plugin){
		String soundName = config.getString(contentPrefix+".name");
		Double pitch = config.getDouble(contentPrefix+".pitch");
		Double volume = config.getDouble(contentPrefix+".volume");
		try {
			if (soundName != null){
				if (Stream.of(Sound.values()).map(Enum::name).collect(Collectors.toList()).contains(soundName.toUpperCase())) {
					if (pitch != null && pitch != 0 && volume != null && volume != 0) {
						return new SoundData(Sound.valueOf(soundName.toUpperCase()), pitch.floatValue(), volume.floatValue());
					} else {
						return new SoundData(Sound.valueOf(soundName.toUpperCase()));
					}
				}
			} else {
				return null;
			}
		} catch (Exception e){
			Bukkit.getLogger().info("[MoxLibrary] Plugin: \""+plugin.getName()+"\" caused an exception while loading SoundData. Details are below.");
			e.printStackTrace();
		}
		Bukkit.getLogger().info("[MoxLibrary] Sound named \""+soundName+"\" has been not found! Caused by: \""+plugin.getName()+"\". (Configuration selection: \""+contentPrefix+"\")");
		return null;
	}

	/**
	 * This method is used to create itemstack from data written in FileConfiguration.
	 *
	 * Reqiured in yml: "material", "amount", "data", "itemName", "itemLore", "glow".
	 *
	 * Optionals: "potion-data" (with required: "effect", "duration", "amplifer", and optional: "color" in format "r,g,b"),
	 * "enchants" (with String list in format "ENCHANTMENT NR").
	 *
	 * @param contentPrefix Location in yaml where is located ItemStack data to load,
	 *                         for example: "path.mysuperitem". Do not place dot at the end!
	 */
	@Nullable
	public static ItemStack loadItemStack(FileConfiguration config, String contentPrefix, Plugin plugin){
		// ItemStack
		String materialTxt = config.getString(contentPrefix + ".material");
		//Integer id = config.getInt(contentPrefix + ".id");
		Integer amount = config.getInt(contentPrefix + ".amount");
		Integer data = config.getInt(contentPrefix + ".data");
		String itemName = config.getString(contentPrefix + ".name");
		String itemLore = config.getString(contentPrefix + ".lore");
		Boolean glow = config.getBoolean(contentPrefix + ".glow");
		ItemStack itemStack;
		if (materialTxt != null) {
			Material material = Material.getMaterial(materialTxt.toUpperCase());
			if (data == null){
				data = 0;
			}
			if (amount == null){
				amount = 1;
			}
			if (material == null){
				Bukkit.getLogger().info(ServerUtils.constructExceptionCause(plugin,
						"[MoxLibrary] Given material name: "+materialTxt.toUpperCase()+" is undefined in Bukkit enums.\n" +
								"Problem found in config section \""+contentPrefix+"\"."));
				return null;
			}
			itemStack = ItemUtils.createItemStack(material, amount, data, itemName, itemLore, glow);
//		} else if (id != null){
//			itemStack = itemUtils.createItemStack(id, amount, data, itemName, itemLore, glow);
		} else {
				Bukkit.getLogger().info("[MoxLibrary] There is no required material or ID data to load and create item stack. " +
						"Caused by: "+plugin.getName()+". ");
			return null;
		}
		if (ItemUtils.isPotion(itemStack)) {
			String potionEffect = config.getString(contentPrefix + ".potion-data.effect");
			Integer potionDuration = config.getInt(contentPrefix + ".potion-data.duration");
			Integer potionAmplifer = config.getInt(contentPrefix + ".potion-data.amplifer");
			String potionColorRaw = config.getString(contentPrefix + ".potion-data.color");
			if (potionEffect != null && potionDuration != null && potionAmplifer != null) {
				itemStack = ItemUtils.addPotionEffect(itemStack, PotionEffectType.getByName(potionEffect), potionDuration, potionAmplifer);
				if (potionColorRaw != null){
					String[] potionColorSplit = potionColorRaw.replace(" ", "").split(",");
					int colorRed = Integer.valueOf(potionColorSplit[0]);
					int colorGreen = Integer.valueOf(potionColorSplit[1]);
					int colorBlue = Integer.valueOf(potionColorSplit[2]);
					Color color = Color.fromRGB(colorRed, colorGreen, colorBlue);
					itemStack = ItemUtils.setPotionColor(itemStack, color);
				}
			}
		}
		// Enchants
		List<String> rawEnchants = config.getStringList(contentPrefix + ".enchants");
		Map<Enchantment, Integer> enchants;
		enchants = transformConfigEnchants(rawEnchants, plugin);
		for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
			itemStack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
		}
		return itemStack;
	}

	/**
	 * This method transfers String enchantments that are in format "ENCHANTMENT N",
	 * for example "FIRE_ASPECT 1" to normal enchantments with level.
	 * Plugin parameter is needed to find in which plugin there is mistake. It can save a lot of time.
	 */
	public static Map<Enchantment, Integer> transformConfigEnchants(List<String> enchantList, Plugin plugin){
		Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
		for (String stringEnchant : enchantList) {
			String[] splitEnchant = stringEnchant.split(" ");
			if (splitEnchant.length != 2) {
				Bukkit.getLogger().warning("[MoxLibrary] Error while loading enchantment at "+stringEnchant+". " +
							"There is no space, no enchant number or more/less than 2 values (enchantment <number>). " +
							"Caused by plugin: "+plugin.getName()+". ");
				continue;
			}
			Integer enchantLevel = Integer.valueOf(splitEnchant[1]);
			String enchantName = splitEnchant[0].toUpperCase();
			if (enchantLevel == null || enchantLevel <= 0) {
				Bukkit.getLogger().warning("[MoxLibrary] Error while loading enchantment "+stringEnchant+". " +
							"There is problem with loading enchantment level of "+enchantName+". " +
							"Caused by plugin: "+plugin.getName()+". ");
				continue;
			}
			Enchantment enchant = Enchantment.getByName(enchantName);
			if (enchant == null) {
				Bukkit.getLogger().warning("[MoxLibrary] Error while loading enchantment "+stringEnchant +". " +
							"Enchant name not found! ("+enchantName+"). " +
							"Please use bukkit enum names! Google: Enchantments bukkit. " +
							"Caused by plugin: "+plugin.getName()+". ");
				continue;
			}
			enchants.put(enchant, enchantLevel);
		}
		return enchants;
	}

}
