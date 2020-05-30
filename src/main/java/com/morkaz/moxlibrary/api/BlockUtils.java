package com.morkaz.moxlibrary.api;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {

	public static Boolean replaceInSignLines(Sign signState, String from, String target) {
		if (signState != null){
			int counter = 0; // first line is 0
			for (String line : signState.getLines()){
				signState.setLine(counter, line.replace(from, target));
				counter++;
			}
			return true;
		}
		return false;
	}

	public static List<Block> getNearbyBlocks(Location location, int radius) {
		List<Block> blocks = new ArrayList<>();
		for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
			for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
				for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
					blocks.add(location.getWorld().getBlockAt(x, y, z));
				}
			}
		}
		return blocks;
	}

	public static Block getRelativeByFace(Block block, BlockFace blockFace, int offsetX, int offsetY, int offsetZ){
		if (blockFace == BlockFace.NORTH){
			return block.getRelative(offsetX, offsetY, -offsetZ);
		} else if (blockFace == BlockFace.SOUTH){
			return block.getRelative(-offsetX, offsetY, offsetZ);
		} else if (blockFace == BlockFace.EAST){
			return block.getRelative(offsetZ, offsetY, offsetX);
		} else if (blockFace == BlockFace.WEST){
			return block.getRelative(-offsetZ, offsetY, -offsetX);
		} else if (blockFace == BlockFace.UP){
			return block.getRelative(offsetX, offsetZ, offsetY);
		} else if (blockFace == BlockFace.DOWN){
			return block.getRelative(offsetX, -offsetZ, -offsetY);
		}
		return block.getRelative(offsetX, offsetY, offsetZ);
	}


}
