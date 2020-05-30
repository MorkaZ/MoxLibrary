package com.morkaz.moxlibrary.data;

import com.morkaz.moxlibrary.api.ItemUtils;
import com.morkaz.moxlibrary.api.MathUtils;
import com.morkaz.moxlibrary.data.enums.EnchantmentCategory;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class MoxEnchantStorage {

	private Enchantment enchantment;
	private EnchantmentCategory enchantmentCategory = null;
	private Integer minRandom = 1, maxRandom = 1;
	private Boolean generateSelfAfter = false, isRandom = false, safeMaxEnchants = true;

	public MoxEnchantStorage(Enchantment enchantment){ // When it will be normal enchant
		this.enchantment = enchantment;
	}

	public MoxEnchantStorage(EnchantmentCategory enchantmentCategory, Integer minRandom, Integer maxRandom){ // When RANDOM_ will be detected
		this.enchantmentCategory = enchantmentCategory;
		this.minRandom = minRandom;
		this.maxRandom = maxRandom;
		this.isRandom = true;
		generateRandomEnchant();
	}

	public MoxEnchantStorage(EnchantmentCategory enchantmentCategory, Integer minRandom, Integer maxRandom, Boolean safeMaxEnchants){ // When RANDOM_ will be detected
		this.enchantmentCategory = enchantmentCategory;
		this.minRandom = minRandom;
		this.maxRandom = maxRandom;
		this.isRandom = true;
		this.safeMaxEnchants = safeMaxEnchants;
		generateRandomEnchant();
	}

	public MoxEnchantStorage(EnchantmentCategory enchantmentCategory, Integer minRandom, Integer maxRandom, Boolean safeMaxEnchants, Boolean generateSelfAfter){ // When RANDOM_ will be detected
		this.enchantmentCategory = enchantmentCategory;
		this.minRandom = minRandom;
		this.maxRandom = maxRandom;
		this.isRandom = true;
		this.safeMaxEnchants = safeMaxEnchants;
		this.generateSelfAfter = generateSelfAfter;
		if (!generateSelfAfter){
			generateRandomEnchant();
		}
	}

	public Enchantment generateRandomEnchant(){
		if (enchantmentCategory == null){
			return null;
		}
		this.enchantment = enchantmentCategory.getEnchantments().get(MathUtils.randomInteger(0, enchantmentCategory.getEnchantments().size()-1));
		return this.enchantment;
	}

	public Boolean isRandom() {
		return isRandom;
	}

	public Integer getRandomEnchantLevel(){
		if (safeMaxEnchants){
			if (minRandom > enchantment.getMaxLevel()){
				return enchantment.getMaxLevel();
			} else if (maxRandom > enchantment.getMaxLevel()){
				return MathUtils.randomInteger(minRandom, enchantment.getMaxLevel());
			}
			return MathUtils.randomInteger(minRandom, maxRandom);
		}
		return MathUtils.randomInteger(minRandom, maxRandom);
	}

	public Pair<Enchantment, Integer> generateEnchantment(){
		return Pair.of(generateRandomEnchant(), getRandomEnchantLevel());
	}

	public ItemStack scheduleEnchantmentsToGenerate(ItemStack itemStack){
		String serializedRandomEnchantsData = ItemUtils.serializeRandomEnchantments(itemStack, enchantmentCategory, minRandom, maxRandom, safeMaxEnchants);
		return ItemUtils.setCustomTag(itemStack, "moxrandomenchants", serializedRandomEnchantsData);
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public EnchantmentCategory getEnchantmentCategory() {
		return enchantmentCategory;
	}

	public Integer getMinRandom() {
		return minRandom;
	}

	public void setMinRandom(Integer minRandom) {
		this.minRandom = minRandom;
	}

	public Integer getMaxRandom() {
		return maxRandom;
	}

	public void setMaxRandom(Integer maxRandom) {
		this.maxRandom = maxRandom;
	}

	public Boolean getGenerateSelfAfter() {
		return generateSelfAfter;
	}

	public void setGenerateSelfAfter(Boolean generateSelfAfter) {
		this.generateSelfAfter = generateSelfAfter;
	}

	public Boolean areSafeMaxEnchants() {
		return safeMaxEnchants;
	}

	public void setSafeMaxEnchants(Boolean safeMaxEnchants) {
		this.safeMaxEnchants = safeMaxEnchants;
	}
}
