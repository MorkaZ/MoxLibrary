package com.morkaz.moxlibrary.api;

import com.morkaz.moxlibrary.data.ParticleData;
import com.morkaz.moxlibrary.data.SoundData;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
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
		String materialTxt = (config.getString(contentPrefix + ".material")+"").toUpperCase();
		//Integer id = config.getInt(contentPrefix + ".id");
		Integer amount = config.getInt(contentPrefix + ".amount");
		Integer data = config.getInt(contentPrefix + ".data");
		String itemName = config.getString(contentPrefix + ".name");
		String itemLore = config.getString(contentPrefix + ".lore");
		if (itemLore == null || itemLore.equals("") || itemLore.startsWith("MemorySection")){
			itemLore = String.join("||", config.getStringList(contentPrefix + ".lore"));
		}
		Boolean glow = config.getBoolean(contentPrefix + ".glow");
		ItemStack itemStack;
		if (materialTxt == null){
			materialTxt = "STONE";
		} else if (materialTxt.equalsIgnoreCase("")){
			materialTxt = "STONE";
		} else if (Material.getMaterial(materialTxt) == null){
			String pluginName = plugin != null ? plugin.getName() : "<UNDEFINED>"+".";
			Bukkit.getLogger().info("[MoxLibrary] Given material name: "+materialTxt.toUpperCase()+" is undefined in Bukkit enums.\n"
					+ "Problem found in config section \""+contentPrefix+"\" of plugin: \""+pluginName+"\"");
			materialTxt = "STONE";
		}
		Material material = Material.getMaterial(materialTxt.toUpperCase());
		if (data == null){
			data = 0;
		}
		if (amount == null){
			amount = 1;
		}
		itemStack = ItemUtils.createItemStack(material, amount, data, itemName, itemLore, glow);
		if (ItemUtils.isPotion(itemStack)) {
			String potionEffect = config.getString(contentPrefix + ".potion-data.effect");
			Integer potionDuration = config.getInt(contentPrefix + ".potion-data.duration");
			Integer potionAmplifer = config.getInt(contentPrefix + ".potion-data.amplifer");
			if (potionEffect != null && potionDuration != null && potionAmplifer != null) {
				itemStack = ItemUtils.addPotionEffect(itemStack, PotionEffectType.getByName(potionEffect), potionDuration, potionAmplifer);
			}
			Color color = loadColor(config, contentPrefix + ".potion-data.color", Color.fromBGR(50, 50, 200), plugin);
			itemStack = ItemUtils.setPotionColor(itemStack, color);
		}
		// TODO ifHead
		// Enchants
		List<String> rawEnchants = config.getStringList(contentPrefix + ".enchants");
		Map<Enchantment, Integer> enchants;
		enchants = transformConfigEnchants(rawEnchants, plugin);
		for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
			itemStack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
		}
		return itemStack;
	}

	public static ParticleData loadParticleData(FileConfiguration config, String contentPrefix, Plugin plugin){
		Particle particle = Particle.SPELL;
		String particleString = config.getString(contentPrefix+".particle");
		if (ToolBox.enumContains(Particle.class, particleString)){
			particle = Particle.valueOf(particleString);
		}
		return loadParticleData(config, contentPrefix, particle, plugin);
	}

	public static ParticleData loadParticleData(FileConfiguration config, String contentPrefix, Particle particle, Plugin plugin){
		Double offsetX = config.getDouble(contentPrefix+".offset-X");
		Double offsetY = config.getDouble(contentPrefix+".offset-Y");
		Double offsetZ = config.getDouble(contentPrefix+".offset-Z");
		Integer amount = config.getInt(contentPrefix+".amount");
		Double extra = config.getDouble(contentPrefix+".extra");
		Object data = null;
		if (particle.getDataType() != Void.class){
			if (particle.getDataType() == Particle.DustOptions.class){
				Integer size = config.getInt("default-setting.data.dust-options.size");
				if (size == null){
					size = 1;
				}
				Color color = loadColor(config, contentPrefix+".data.dust-options", plugin);
				data = new Particle.DustOptions(color, size);
			} else if (particle.getDataType() == ItemStack.class){
				data = ConfigUtils.loadItemStack(config, contentPrefix+".data.item", plugin);
			} else if (particle.getDataType() == BlockData.class){
				String materialString = config.getString(contentPrefix+".data.block-data.material");
				Material material = Material.STONE;
				if (materialString != null && ToolBox.enumContains(Material.class, materialString)){
					material = Material.valueOf(config.getString(contentPrefix+".data.block-data.material"));
				}
				data = material.createBlockData();
			}
		}
		return new ParticleData(particle, null, amount, offsetX, offsetY, offsetZ, extra, data);
	}

	public static Color loadColor(FileConfiguration config, String contentPrefix, Plugin plugin){
		return loadColor(config, contentPrefix, Color.WHITE, plugin);
	}

	public static Color loadColor(FileConfiguration config, String contentPrefix, Color defaultColor, Plugin plugin) {
		Integer red = defaultColor.getRed(), green = defaultColor.getGreen(), blue = defaultColor.getBlue();
		if (config == null){
			return Color.fromRGB(red, green, blue);
		}
		String colorString = config.getString(contentPrefix);
		if (colorString != null && !colorString.equalsIgnoreCase("") && !colorString.startsWith("MemorySection")) {
			String[] colorStringSplited = colorString.replace(" ", "").split(",");
			if (colorStringSplited.length >= 1) {
				if (NumberUtils.isNumber(colorStringSplited[0])) {
					red = Integer.valueOf(colorStringSplited[0]);
				}
			}
			if (colorStringSplited.length >= 2) {
				if (NumberUtils.isNumber(colorStringSplited[1])) {
					green = Integer.valueOf(colorStringSplited[1]);
				}
			}
			if (colorStringSplited.length == 3) {
				if (NumberUtils.isNumber(colorStringSplited[2])) {
					red = Integer.valueOf(colorStringSplited[2]);
				}
			}
			return Color.fromRGB(red, green, blue);
		}
		ConfigurationSection configurationSection = config.getConfigurationSection(contentPrefix);
		if (configurationSection != null && configurationSection.getKeys(false).size() > 0) {
			Integer configRed = config.getInt(contentPrefix + ".red");
			Integer configGreen = config.getInt(contentPrefix + ".green");
			Integer configBlue = config.getInt(contentPrefix + ".blue");
			if (configRed != null) {
				red = configRed;
			}
			if (configGreen != null) {
				green = configGreen;
			}
			if (configBlue != null) {
				blue = configBlue;
			}
		}
		return Color.fromRGB(red, green, blue);
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
				Bukkit.getLogger().warning("[MoxLibrary] Error while loading enchantment at " + stringEnchant + ". " +
						"There is no space, no enchant number or more/less than 2 values (enchantment <number>). " +
						"Caused by plugin: " + plugin.getName() + ". ");
				continue;
			}
			Integer enchantLevel = Integer.valueOf(splitEnchant[1]);
			String enchantName = splitEnchant[0].toUpperCase();
			if (enchantLevel == null || enchantLevel <= 0) {
				Bukkit.getLogger().warning("[MoxLibrary] Error while loading enchantment " + stringEnchant + ". " +
						"There is problem with loading enchantment level of " + enchantName + ". " +
						"Caused by plugin: " + plugin.getName() + ". ");
				continue;
			}
			Enchantment enchant = Enchantment.getByName(enchantName);
			if (enchant == null) {
				Bukkit.getLogger().warning("[MoxLibrary] Error while loading enchantment " + stringEnchant + ". " +
						"Enchant name not found! (" + enchantName + "). " +
						"Please use bukkit enum names! Google: Enchantments bukkit. " +
						"Caused by plugin: " + plugin.getName() + ". ");
				continue;
			}
			enchants.put(enchant, enchantLevel);
		}
		return enchants;
	}
}
