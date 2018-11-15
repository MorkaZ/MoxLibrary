package com.morkaz.moxlibrary.api;

import de.tr7zw.itemnbtapi.NBTItem;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagList;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.Arrays;


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
		PotionMeta itemMeta = (PotionMeta)itemStack.getItemMeta();
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
		net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = null;
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		}
		if (tag == null) tag = nmsStack.getTag();
		NBTTagList ench = new NBTTagList();
		tag.set("ench", ench);
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
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



	// -------------------
	// ITEM META
	// -------------------

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
