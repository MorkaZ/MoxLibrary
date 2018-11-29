package com.morkaz.moxlibrary.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ActionItem {

	private ItemStack itemStack;
	private Integer slot;

	public ActionItem(Integer x, Integer y, ItemStack itemStack){
		this.slot = x*y;
		this.itemStack = itemStack;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public Integer getSlot() {
		return slot;
	}

	public abstract void onClick(InventoryClickEvent event);



}
