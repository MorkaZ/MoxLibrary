package com.morkaz.moxlibrary.stuff;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_15_R1.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Title {

	private String title, subtitle;
	private int fadeIn, stay, fadeOut;

	public Title(String title, String subTitle, Integer titleStayTicks) {
		this.title = ChatColor.translateAlternateColorCodes('&', title);
		this.subtitle = ChatColor.translateAlternateColorCodes('&', subTitle);
		this.fadeIn = 10;
		this.stay = titleStayTicks;
		this.fadeOut = 10;
	}
	
	public void send(Player player) {
		//Tre�ci
		IChatBaseComponent titleText = ChatSerializer.a("{\"text\": \"" + title.replace("\"", "") + "\"}");
		IChatBaseComponent subtitleText = ChatSerializer.a("{\"text\": \"" + subtitle.replace("\"", "") + "\"}");
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleText);
		PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleText);
		PacketPlayOutTitle lengthPacket = new PacketPlayOutTitle(this.fadeIn, stay, this.fadeOut);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(titlePacket);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(subtitlePacket);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(lengthPacket);
	}
	
	public void send(ArrayList<Player> players) {
		IChatBaseComponent titleText = ChatSerializer.a("{\"text\": \"" + title.replace("\"", "") + "\"}");
		IChatBaseComponent subtitleText = ChatSerializer.a("{\"text\": \"" + subtitle.replace("\"", "") + "\"}");
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleText);
		PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleText);
		PacketPlayOutTitle lengthPacket = new PacketPlayOutTitle(this.fadeIn, stay, this.fadeOut);
		for (Player player : players) {
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(titlePacket);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(subtitlePacket);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(lengthPacket);
		}
	}
	
	public void sendToAll() {
		IChatBaseComponent titleText = ChatSerializer.a("{\"text\": \"" + title.replace("\"", "") + "\"}");
		IChatBaseComponent subtitleText = ChatSerializer.a("{\"text\": \"" + subtitle.replace("\"", "") + "\"}");
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleText);
		PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleText);
		PacketPlayOutTitle lengthPacket = new PacketPlayOutTitle(this.fadeIn * 20, stay * 20, this.fadeOut * 20);
		for (Player player : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(titlePacket);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(subtitlePacket);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(lengthPacket);
		}
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public int getFadeIn() {
		return fadeIn;
	}

	public void setFadeIn(int fadeIn) {
		this.fadeIn = fadeIn;
	}

	public int getStay() {
		return stay;
	}

	public void setStay(int stay) {
		this.stay = stay;
	}

	public int getFadeOut() {
		return fadeOut;
	}

	public void setFadeOut(int fadeOut) {
		this.fadeOut = fadeOut;
	}
	
}
