package com.morkaz.moxlibrary.api;

import net.minecraft.server.v1_13_R2.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerUtils {


	public static String dateFormatFromUnix(Long unixTime, String dateFormat){
		if (dateFormat == null){
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		Date date = new Date(unixTime);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String format = sdf.format(date);
		return format;
	}

	public static PluginCommand registerCommand(Plugin plugin, String name, List<String> aliases, String description, String usage){
		final PluginCommand cmd = createPluginCommand(name, plugin);
		if (cmd != null) {
			cmd.setAliases(aliases)
					.setUsage(usage)
					.setDescription(description);
			CommandMap commandMap = getCommandMapInstance();
			if (commandMap != null) {
				commandMap.register(plugin.getDescription().getName(), cmd);
			}
		}
		return cmd;
	}

	public static String constructExceptionCause(Plugin plugin, String cause){
		String text = cause + " \nCaused by: "+plugin.getName()+". ";
		return text;
	}

	public static void setTablistHeaderFooter(Player player, String header, String footer){
		setTablistFooter(player, footer);
		setTablistHeader(player, header);
	}

	public static void setTablistFooter(Player player, String footer){
		CraftPlayer cplayer = (CraftPlayer) player;
		PlayerConnection connection = cplayer.getHandle().playerConnection;
		IChatBaseComponent bottom = IChatBaseComponent.ChatSerializer.a("{text: '"+footer+"'}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		try {
			Field headerField = packet.getClass().getDeclaredField("b");
			headerField.setAccessible(true);
			headerField.set(packet, bottom);
			headerField.setAccessible(!headerField.isAccessible());
		} catch (Exception e) {
			e.printStackTrace();
		}
		connection.sendPacket(packet);
	}

	public static void setTablistHeader(Player player, String header){
		CraftPlayer cplayer = (CraftPlayer) player;
		PlayerConnection connection = cplayer.getHandle().playerConnection;
		IChatBaseComponent top = IChatBaseComponent.ChatSerializer.a("{text: '"+header+"'}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		try {
			Field headerField = packet.getClass().getDeclaredField("a");
			headerField.setAccessible(true);
			headerField.set(packet, top);
			headerField.setAccessible(!headerField.isAccessible());
		} catch (Exception e) {
			e.printStackTrace();
		}
		connection.sendPacket(packet);
	}
    
    public static void sendMessage(ArrayList<Player> receivers, String prefix, String playerPlaceholder, String message) {
    	message = ChatColor.translateAlternateColorCodes('&', prefix+message.replace("%player%", playerPlaceholder));
    	for (Player receiver : receivers) {
    		receiver.sendMessage(message);
    	}	
    }
    
    public static void sendMessage(CommandSender receiver, String prefix, String playerPlaceholder, String message) {
    	message = ChatColor.translateAlternateColorCodes('&', prefix+message.replace("%player%", playerPlaceholder));
    	receiver.sendMessage(message);
    }
    
    public static void sendMessage(CommandSender receiver, String prefix, String message) {
    	message = ChatColor.translateAlternateColorCodes('&', prefix+message);
    	receiver.sendMessage(message);
    }
    
    public static void sendMessage(ArrayList<Player> receivers, String prefix, String message) {
    	final String messageFinal = ChatColor.translateAlternateColorCodes('&', prefix+message);
    	receivers.forEach((receiver) -> {
    		receiver.sendMessage(messageFinal);
    	});
    }
    
    public static void sendMessage(CommandSender receiver, String message) {
    	message = ChatColor.translateAlternateColorCodes('&', message);
    	receiver.sendMessage(message);
    }
    
    public static void sendMessage(ArrayList<Player> receivers, String message) {
    	final String messageFinal = ChatColor.translateAlternateColorCodes('&', message);
    	receivers.forEach((receiver) -> {
    		receiver.sendMessage(messageFinal);
    	});
    }


	private static CommandMap getCommandMapInstance() {
		if ( Bukkit.getPluginManager() instanceof SimplePluginManager) {
			SimplePluginManager spm = (SimplePluginManager) Bukkit.getPluginManager();
			try {
				Field field = FieldUtils.getDeclaredField(spm.getClass(), "commandMap", true);
				return (CommandMap) field.get( spm );
			} catch (IllegalAccessException e) {
				throw new RuntimeException( "Can't get the Bukkit CommandMap instance." );
			}
		}
		return null;
	}


	private static PluginCommand createPluginCommand(String name, Plugin plugin) {
		try {
			Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor( String.class, Plugin.class );
			constructor.setAccessible( true );
			return constructor.newInstance(name, plugin);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
    
}
