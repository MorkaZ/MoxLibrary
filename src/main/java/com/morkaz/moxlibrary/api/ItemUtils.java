package com.morkaz.moxlibrary.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.morkaz.moxlibrary.data.MoxEnchantStorage;
import com.morkaz.moxlibrary.data.enums.EnchantmentCategory;
import com.morkaz.moxlibrary.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


public class ItemUtils {

	// -------------------
	// MISC
	// -------------------

	/** Dedicated for potions and heads. This items can contain in some situations tag called "internal".
	 * This tag makes a lot of problems because it is not always written in item meta.
	 * This method is comparing items that may have internal tag. */
	public static Boolean compareItemStacks(ItemStack item1, ItemStack item2) {
		String item1String = item1.toString(), item2String = item2.toString();
		if (item1String.contains(", internal=")) {
			String[] split = item1String.split("internal=");
			item1String = split[0] + split[1].split(", ", 2)[1];
		}
		if (item2String.contains(", internal=")) {
			String[] split = item2String.split("internal=");
			item2String = split[0] + split[1].split(", ", 2)[1];
		}
		return item1String.equals(item2String);
	}

	public static Boolean isItemWithInternalTag(ItemStack item){
		String itemString = item.toString();
		if (itemString.contains(", internal=")) {
			return true;
		}
		return false;
	}


	// -------------------
	// CUSTOM TEXTURED HEADS
	// -------------------

	public static ItemStack setHeadTexture(ItemStack head, String value) {
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), "");
		profile.getProperties().put("textures", new Property("textures", value));
		Field profileField = null;
		try {
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		head.setItemMeta(meta);
		return head;
	}


	// -------------------
	// POTIONS
	// -------------------

	public static boolean isPotion(ItemStack itemStack){
		if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.LINGERING_POTION){
			return true;
		}
		return false;
	}

	public static ItemStack createPotionItemStack(Material potionType, int amount, PotionEffectType potionEffect, int duration, int amplifier){
		if (potionType != Material.POTION && potionType != Material.SPLASH_POTION && potionType != Material.LINGERING_POTION ){
			return null;
		}
		ItemStack itemStack = new ItemStack(potionType, amount);
		itemStack = addPotionEffect(itemStack, potionEffect, duration, amplifier);
		return itemStack;
	}

	public static ItemStack addPotionEffect(ItemStack itemStack, PotionEffectType potionEffect, Integer duration, Integer amplifier){
		PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
		meta.addCustomEffect(new PotionEffect(potionEffect, duration, amplifier), true);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static ItemStack setPotionColor(ItemStack itemStack, Color color){
		PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
		meta.setColor(color);
		itemStack.setItemMeta(meta);
		return itemStack;
	}



	// -------------------
	// ITEM TAG
	// -------------------

	public static ItemStack addGlow(ItemStack itemStack){
		// TODO for all versions
		return itemStack;
	}

	public static NBTItem getNBTItem(ItemStack itemStack){
		return new NBTItem(itemStack);
	}

	public static ItemStack setCustomTag(ItemStack itemStack, String tagKey, String tagValue){
		NBTItem nbtItem = getNBTItem(itemStack);
		nbtItem.setString(tagKey, tagValue);
		return nbtItem.getItem();
	}

	public static ItemStack setCustomTagObject(ItemStack itemStack, String tagKey, Object tagValue){
		NBTItem nbtItem = getNBTItem(itemStack);
		nbtItem.setObject(tagKey, tagValue);
		return nbtItem.getItem();
	}

	@Nullable
	public static String getCustomTag(ItemStack itemStack, String tagKey){
		NBTItem nbtItem = getNBTItem(itemStack);
		return nbtItem.getString(tagKey);
	}

	@Nullable
	public static String getCustomTagObject(ItemStack itemStack, String tagKey, Class<String> type){
		NBTItem nbtItem = getNBTItem(itemStack);
		return nbtItem.getObject(tagKey, type);
	}

	public static ItemStack removeCustomTag(ItemStack itemStack, String tagKey){
		NBTItem nbtItem = getNBTItem(itemStack);
		nbtItem.removeKey(tagKey);
		return nbtItem.getItem();
	}



	// -------------------
	// ITEM META
	// -------------------

	public static ItemStack applyScheduledRandomEnchantments(ItemStack itemStack){
		List<Pair<Enchantment, Integer>> enchants = generateScheduledRandomEnchantments(itemStack);
		for (Pair<Enchantment, Integer> pair : enchants){
			itemStack.addUnsafeEnchantment(pair.getLeft(), pair.getRight());
		}
		itemStack = ItemUtils.removeCustomTag(itemStack, "moxrandomenchants");
		return itemStack;
	}

	public static List<Pair<Enchantment, Integer>> generateScheduledRandomEnchantments(ItemStack itemStack){
		String textRandom = deserializeRandomEnchantments(itemStack);
		if (textRandom == null || textRandom.equals("")){
			return null;
		}
		String[] split = textRandom.split("\\|\\|");
		List<MoxEnchantStorage> enchantStorages = new ArrayList<>();
		for (String dataLine : split){
			String[] splitedDataLine = dataLine.split(",");
			EnchantmentCategory category = EnchantmentCategory.valueOf(splitedDataLine[0]);
			Integer min = Integer.valueOf(splitedDataLine[1]);
			Integer max = Integer.valueOf(splitedDataLine[2]);
			Boolean safeMaxEnchants = Boolean.valueOf(splitedDataLine[3]);
			enchantStorages.add(new MoxEnchantStorage(category, min, max, safeMaxEnchants));
		}
		List<Pair<Enchantment, Integer>> enchantmentsList = new ArrayList<>();
		for (MoxEnchantStorage enchantStorage : enchantStorages){
			enchantmentsList.add(enchantStorage.generateEnchantment());
		}
		return enchantmentsList;
	}

	public static String serializeRandomEnchantments(ItemStack itemStack, EnchantmentCategory enchantmentCategory, Integer minRandom, Integer maxRandom, Boolean safeMaxEnchants){
		String savedRandomEnchants = deserializeRandomEnchantments(itemStack);
		if (savedRandomEnchants == null){
			return null;
		}
		if (savedRandomEnchants.equals("")){
			savedRandomEnchants =  enchantmentCategory.toString() +","+ minRandom.toString()+","+maxRandom.toString()+","+safeMaxEnchants.toString();
		} else {
			savedRandomEnchants = savedRandomEnchants + "||" + enchantmentCategory.toString() +","+ minRandom.toString()+","+maxRandom.toString()+","+safeMaxEnchants.toString();
		}
		ByteArrayOutputStream str = new ByteArrayOutputStream();
		try {
			BukkitObjectOutputStream data = new BukkitObjectOutputStream(str);
			data.writeObject(savedRandomEnchants);
			String encodedString = Base64.getEncoder().encodeToString(str.toByteArray());
			data.close();
			return encodedString;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String deserializeRandomEnchantments(ItemStack itemStack){
		String nbt = ItemUtils.getCustomTag(itemStack, "moxrandomenchants");
		if (nbt == null || nbt.equals("")){
			return "";
		}
		ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(nbt));
		try {
			BukkitObjectInputStream data = new BukkitObjectInputStream(stream);
			String savedRandomEnchants = data.readObject().toString();
			data.close();
			return savedRandomEnchants;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ItemStack replaceInLore(ItemStack itemStack, String target, String replacement){
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> lore = itemMeta.getLore();
		List<String> newLore = new ArrayList<>();
		for (String loreLine : lore){
			newLore.add(loreLine.replace(target, replacement));
		}
		itemMeta.setLore(newLore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack setItemName(ItemStack itemStack, String itemName){
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (itemName != null && !itemName.equals("")) {
			itemName = ChatColor.translateAlternateColorCodes('&', itemName);
			itemMeta.setDisplayName(itemName);
		}
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack setItemLore(ItemStack itemStack, String itemLore){
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (itemLore != null && !itemLore.equals("")) {
			itemLore = ChatColor.translateAlternateColorCodes('&', itemLore);
			String[] loreSplit = itemLore.split("\\|\\|");
			itemMeta.setLore(Arrays.asList(loreSplit));
		}
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack setItemNameLore(ItemStack itemStack, String itemName, String itemLore){
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (itemLore != null && !itemLore.equals("")) {
			itemLore = ChatColor.translateAlternateColorCodes('&', itemLore);
			String[] loreSplit = itemLore.split("\\|\\|");
			itemMeta.setLore(Arrays.asList(loreSplit));
		}
		if (itemName != null && !itemName.equals("")) {
			itemName = ChatColor.translateAlternateColorCodes('&', itemName);
			itemMeta.setDisplayName(itemName);
		}
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack modifyItemStack(ItemStack itemStack, Integer itemData, String itemName, String itemLore, Boolean glow){
		if (itemData != null && itemData != 0) {
			itemStack.setDurability(itemData.shortValue());
		}
		itemStack = setItemNameLore(itemStack, itemName, itemLore);
		if (glow) {
			itemStack = addGlow(itemStack);
		}
		return itemStack;
	}



	// -------------------
	// SMART ITEM CREATING
	// -------------------

//	/** Do not use it. Item ids will be removed in mc 1.13. */
//	@Deprecated
//	public ItemStack createItemStack(Integer id, Integer amount, Integer itemData, String itemName, String itemLore, Boolean glow) {
//		ItemStack itemStack = new ItemStack(id, amount);
//		itemStack = modifyItemStack(itemStack, itemData, itemName, itemLore, glow);
//		return itemStack;
//	}

	public static ItemStack createItemStack(Material material, Integer amount, Integer itemData, String itemName, String itemLore, Boolean glow) {
		ItemStack itemStack = new ItemStack(material, amount);
		itemStack = modifyItemStack(itemStack, itemData, itemName, itemLore, glow);
		return itemStack;
	}

	public static ItemStack createItemStack(Material material, Integer amount, String itemName, String itemLore){
		ItemStack newItem = new ItemStack(material, amount);
		newItem = setItemNameLore(newItem, itemName, itemLore);
		return newItem;
	}

	public static ItemStack createItemStack(Material material, Integer amount){
		return new ItemStack(material, amount);
	}

}