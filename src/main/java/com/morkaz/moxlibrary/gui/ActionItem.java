package com.morkaz.moxlibrary.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ActionItem {

	private ItemStack itemStack;
	private Integer slot;

	public ActionItem(Integer x, Integer y, ItemStack itemStack){
		this.slot = ((y-1)*9) + (x-1);
		this.itemStack = itemStack;
	}

	public ActionItem(Integer slot, ItemStack itemStack){
		this.slot = slot;
		this.itemStack = itemStack;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public Integer getSlot() {
		return slot;
	}

	public Integer getX(){
		return this.slot % 9;
	}

	public Integer getY(){
		return (slot/9) - getX();
	}

	public abstract void onClick(InventoryClickEvent event);



}
