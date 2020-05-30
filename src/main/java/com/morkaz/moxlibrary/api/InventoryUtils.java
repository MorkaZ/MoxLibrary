package com.morkaz.moxlibrary.api;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

public class InventoryUtils {

	// Title field holder
	private static Field titleField;
	private static Method getInventoryMethod;

	// Class must be initialized/loaded once.
	static {
		try {
			Object cinv = Bukkit.createInventory(null, 6*9, "test");
			getInventoryMethod = cinv.getClass().getMethod("getInventory");
			Object minv = getInventoryMethod.invoke(cinv);
			titleField = minv.getClass().getDeclaredField("title");
			titleField.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	// -------------------
	// SERIALIZATION / INVENTORY STUFF
	// -------------------

	public static String getInventoryTitle(Inventory inventory){
		try {
			Object minv = getInventoryMethod.invoke(inventory);
			String title = (String) titleField.get(minv);
			return title;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	// Serialize Inventory data with item objects
	public static String inventoryToBase64(Inventory inventory) {
		try {
			ByteArrayOutputStream str = new ByteArrayOutputStream();
			BukkitObjectOutputStream data = new BukkitObjectOutputStream(str);
			data.writeInt(inventory.getSize());
			String inventoryName = getInventoryTitle(inventory);
			data.writeObject(inventoryName);
			for (int i = 0; i < inventory.getSize(); i++) {
				data.writeObject(inventory.getItem(i));
			}
			data.close();
			return Base64.getEncoder().encodeToString(str.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Deserialize inventory data and item objects and create new Inventory
	public static Inventory base64ToInventory(String base64InventoryString) {
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(base64InventoryString));
			BukkitObjectInputStream data = new BukkitObjectInputStream(stream);
			Inventory inventory = Bukkit.createInventory(null, data.readInt(), data.readObject().toString());
			for (int i = 0; i < inventory.getSize(); i++) {
				inventory.setItem(i, (ItemStack) data.readObject());
			}
			data.close();
			return inventory;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
