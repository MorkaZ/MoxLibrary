package com.morkaz.moxlibrary.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestGUI {

	//It will initialize once on class load.
	//It will not be linked to object, but to class.
	public static Map<Inventory, ChestGUI> chestGUIMap = new HashMap<>();
	//It has to be initialized once.
	public static GUIListener guiListener = null;

	private Integer size;
	private String title;
	private Plugin plugin;
	private Inventory inventory;
	private Boolean unregisterOnClose;
	//          Slot     ActionItem
	public Map<Integer, ActionItem> actionItemMap = new HashMap<>();

	public ChestGUI(Plugin plugin, Integer size, String title, Boolean unregisterOnClose) {
		this.size = size;
		this.title = ChatColor.translateAlternateColorCodes('&', title+"");
		this.plugin = plugin;
		this.unregisterOnClose = unregisterOnClose;
		this.inventory = Bukkit.createInventory(null, size*9, title);
		chestGUIMap.put(inventory, this);
		if (guiListener == null){
			guiListener = new GUIListener(plugin);
		}
	}

	public Boolean getUnregisterOnClose() {
		return unregisterOnClose;
	}

	public Integer getSize() {
		return size;
	}

	public String getTitle() {
		return title;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void open(Player player){
		player.openInventory(inventory);
	}

	public void close(Player player){
		if (player.getOpenInventory().getTopInventory().equals(inventory)){
			player.closeInventory();
		}
	}

	public List<HumanEntity> getViewers(){
		return this.inventory.getViewers();
	}

	public void setItem(ActionItem actionItem){
		this.actionItemMap.put(actionItem.getSlot(), actionItem);
	}

	public void unregisterGUI(){
		chestGUIMap.remove(this.inventory);
	}

}


class GUIListener implements Listener {

	private Plugin plugin;

	public GUIListener(Plugin plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority= EventPriority.HIGHEST)
	public void inventoryClickListener(InventoryClickEvent event){
		ChestGUI chestGUI = ChestGUI.chestGUIMap.get(event.getInventory());
		if (chestGUI != null){
			chestGUI.actionItemMap.get(event.getSlot()).onClick(event);
		}
	}

	@EventHandler(priority= EventPriority.HIGHEST)
	public void inventoryCloseListener(InventoryCloseEvent event){
		ChestGUI chestGUI = ChestGUI.chestGUIMap.get(event.getInventory());
		if (chestGUI != null){
			if (chestGUI.getUnregisterOnClose()){ // && chestGUI.getViewers().size() == 0
				ChestGUI.chestGUIMap.remove(event.getInventory());
			}
		}
	}



}
