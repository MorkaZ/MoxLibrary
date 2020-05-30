package com.morkaz.moxlibrary.api;

import com.morkaz.moxlibrary.data.CommandData;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ServerUtils {

	public static String getPlayerID(Player player){
		return Bukkit.getOnlineMode() ? player.getUniqueId().toString() : player.getName().toLowerCase();
	}

	public static String getPlayerID(String playerName){
		if (Bukkit.getOnlineMode()){
			Player player = Bukkit.getPlayerExact(playerName);
			if (player != null){
				return Bukkit.getPlayerExact(playerName).getUniqueId().toString();
			} else {
				return Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
			}
		} else {
			return playerName.toLowerCase();
		}
	}

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

	public static PluginCommand registerCommand(Plugin plugin, String name, List<String> aliases, String description, String usage, CommandExecutor commandExecutor){
		final PluginCommand cmd = registerCommand(plugin, name, aliases, description, usage);
		if (commandExecutor != null){
			cmd.setExecutor(commandExecutor);
		}
		return cmd;
	}

	public static PluginCommand registerCommand(CommandData commandData){
		return registerCommand(commandData.getPlugin(), commandData.getMainCommand(), commandData.getAliases(), commandData.getDescription(), commandData.getUsage(), commandData.getCommandExecutor());
	}

	public static void setTablistHeaderFooter(Player player, String header, String footer){
		setTablistFooter(player, footer);
		setTablistHeader(player, header);
	}

	public static void setTablistFooter(Player player, String footer){
		player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', footer));
	}

	public static void setTablistHeader(Player player, String header){
		player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&', header));
	}
    
    public static void sendMessage(List<Player> receivers, String prefix, String playerPlaceholder, String message) {
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
    
    public static void sendMessage(List<Player> receivers, String prefix, String message) {
    	final String messageFinal = ChatColor.translateAlternateColorCodes('&', prefix+message);
    	receivers.forEach((receiver) -> {
    		receiver.sendMessage(messageFinal);
    	});
    }
    
    public static void sendMessage(CommandSender receiver, String message) {
    	message = ChatColor.translateAlternateColorCodes('&', message);
    	receiver.sendMessage(message);
    }
    
    public static void sendMessage(List<Player> receivers, String message) {
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
