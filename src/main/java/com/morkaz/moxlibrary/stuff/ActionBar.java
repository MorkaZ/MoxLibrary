package com.morkaz.moxlibrary.stuff;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class ActionBar {

	private String text;
	
	public ActionBar(String text) {
		this.text = ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public void send(Player player) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text.replace("\"", "") + "\"}"), ChatMessageType.GAME_INFO);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void send(List<Player> players) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text.replace("\"", "") + "\"}"), ChatMessageType.GAME_INFO);
		for (Player player : players) {
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	public void sendToAll() {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text.replace("\"", "") + "\"}"), ChatMessageType.GAME_INFO);
		for (Player player : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
