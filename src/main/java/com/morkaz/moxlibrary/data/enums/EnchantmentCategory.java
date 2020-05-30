package com.morkaz.moxlibrary.data.enums;

import com.morkaz.moxlibrary.api.ToolBox;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnchantmentCategory {

	SWORD(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(swordEnchantments);
		}
	},
	BOW(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(bowEnchantments);
		}
	},
	AXE(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(axeEnchantments);
		}
	},
	PICKAXE(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(pickaxeEnchantments);
		}
	},
	ANY_ARMOR(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(armorEnchantments);
		}
	},
	HELMET(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(helmetEnchantments);
		}
	},
	CHESTPLATE(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(chestEnchantments);
		}
	},
	LEGGINGS(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(leggingsEnchantments);
		}
	},
	BOOTS(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(bootsEnchantments);
		}
	},
	FISHING_ROD(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(fishingRodEnchantments);
		}
	},
	TRIDENT(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(tridentEnchantments);
		}
	},
	SHOVEL(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(shovelEnchantments);
		}
	},
	CROSSBOW(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(crossbowEnchantments);
		}
	},
	ANY(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(anyEnchantments);
		}
	},
	UNIQUE(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(uniqueEnchantments);
		}
	},
	UNKOWN(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>();
		}
	},
	ANY_TOOL(){
		public List<Enchantment> getEnchantments() {
			return new ArrayList<>(toolsEnchantments);
		}
	};

	private static List<Enchantment> uniqueEnchantments = Arrays.asList(
			Enchantment.MENDING, Enchantment.VANISHING_CURSE, Enchantment.BINDING_CURSE
	);
	private static List<Enchantment> anyEnchantments = ToolBox.removeFromList(new ArrayList(Arrays.asList(
			Enchantment.values()
	)), uniqueEnchantments);
	private static List<Enchantment> swordEnchantments = Arrays.asList(
			Enchantment.DAMAGE_ARTHROPODS, Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT,
			Enchantment.KNOCKBACK, Enchantment.LOOT_BONUS_MOBS, Enchantment.DURABILITY,
			Enchantment.DAMAGE_UNDEAD, Enchantment.SWEEPING_EDGE
	);
	private static List<Enchantment> bowEnchantments = Arrays.asList(
			Enchantment.ARROW_FIRE, Enchantment.ARROW_INFINITE, Enchantment.DURABILITY,
			Enchantment.ARROW_DAMAGE, Enchantment.ARROW_KNOCKBACK
	);
	private static List<Enchantment> crossbowEnchantments = Arrays.asList(
			Enchantment.MULTISHOT, Enchantment.PIERCING,Enchantment.DURABILITY,
			Enchantment.QUICK_CHARGE
	);
	private static List<Enchantment> armorEnchantments = Arrays.asList(
			Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_FIRE, Enchantment.DURABILITY,
			Enchantment.PROTECTION_PROJECTILE, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.THORNS
	);
	private static List<Enchantment> helmetEnchantments = ToolBox.mergeLists(Arrays.asList(
			Enchantment.WATER_WORKER, Enchantment.OXYGEN
	), armorEnchantments);
	private static List<Enchantment> chestEnchantments = armorEnchantments;
	private static List<Enchantment> leggingsEnchantments = armorEnchantments;
	private static List<Enchantment> bootsEnchantments = ToolBox.mergeLists(Arrays.asList(
			Enchantment.DEPTH_STRIDER, Enchantment.FROST_WALKER, Enchantment.PROTECTION_FALL
	), armorEnchantments);
	private static List<Enchantment> fishingRodEnchantments = Arrays.asList(
			Enchantment.LURE, Enchantment.DURABILITY, Enchantment.LUCK
	);
	private static List<Enchantment> tridentEnchantments = Arrays.asList(
			Enchantment.CHANNELING, Enchantment.IMPALING, Enchantment.LOYALTY,
			Enchantment.RIPTIDE
	);
	private static List<Enchantment> toolsEnchantments = Arrays.asList(
			Enchantment.LOOT_BONUS_BLOCKS, Enchantment.SILK_TOUCH, Enchantment.DURABILITY,
			Enchantment.DIG_SPEED
	);
	private static List<Enchantment> pickaxeEnchantments = toolsEnchantments;
	private static List<Enchantment> axeEnchantments = ToolBox.mergeLists(Arrays.asList(
			Enchantment.DAMAGE_ARTHROPODS, Enchantment.DAMAGE_ARTHROPODS, Enchantment.DAMAGE_ALL
	), toolsEnchantments);
	private static List<Enchantment> shovelEnchantments = toolsEnchantments;



	public abstract List<Enchantment> getEnchantments();

	public static EnchantmentCategory getByMaterial(Material material){
		if (material == null){
			return null;
		}
		if (material.toString().contains("_HELMET")){
			return EnchantmentCategory.HELMET;
		} else if (material.toString().contains("_CHESTPLATE")){
			return EnchantmentCategory.CHESTPLATE;
		} else if (material.toString().contains("_BOOTS")){
			return EnchantmentCategory.BOOTS;
		} else if (material.toString().contains("_LEGGINGS")){
			return EnchantmentCategory.LEGGINGS;
		} else if (material.toString().contains("_PICKAXE")){
			return EnchantmentCategory.PICKAXE;
		} else if (material.toString().contains("_SHOVEL")){
			return EnchantmentCategory.SHOVEL;
		} else if (material.toString().contains("_AXE")){
			return EnchantmentCategory.AXE;
		} else if (material.toString().contains("_SWORD")){
			return EnchantmentCategory.SWORD;
		} else if (material == Material.BOW){
			return EnchantmentCategory.BOW;
		} else if (material == Material.CROSSBOW){
			return EnchantmentCategory.CROSSBOW;
		} else if (material == Material.TRIDENT){
			return EnchantmentCategory.TRIDENT;
		} else if (material == Material.FISHING_ROD){
			return EnchantmentCategory.FISHING_ROD;
		}
		return EnchantmentCategory.ANY;
	}

	public static EnchantmentCategory getByItemStack(ItemStack itemStack){
		if (itemStack == null){
			return null;
		}
		Material material = itemStack.getType();
		return getByMaterial(material);
	}


}
