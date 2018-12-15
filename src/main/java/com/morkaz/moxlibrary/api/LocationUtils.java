package com.morkaz.moxlibrary.api;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

	public static List<Location> getSphere(Location center, Integer radius) {
		List<Location> sphereLocations = new ArrayList<Location>();
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			double circleRadius = Math.sin(i);
			double y = Math.cos(i);
			for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
				double x = Math.cos(a) * circleRadius;
				double z = Math.sin(a) * circleRadius;
				sphereLocations.add(new Location(center.getWorld(), x, y, z));
			}
		}
		return sphereLocations;
	}

	public static List<Location> getCircle(Location center, double radius, int amount) {
		double increment = (2 * Math.PI) / amount;
		List<Location> locations = new ArrayList<Location>();
		for (int i = 0;i < amount; i++) {
			double angle = i * increment;
			double x = center.getX() + (radius * Math.cos(angle));
			double z = center.getZ() + (radius * Math.sin(angle));
			locations.add(new Location(center.getWorld(), x, center.getY(), z));
		}
		return locations;
	}

	public static Block getBlockBehindPlayer(Player player, Number distance) {
		Vector inverseDirectionVec = player.getLocation().getDirection().normalize().multiply(distance.doubleValue()*(-1d));
		return player.getLocation().add(inverseDirectionVec).getBlock();
	}

	public static Location getLocationBehindPlayer(Player player, Number distance) {
		Vector inverseDirectionVec = player.getLocation().getDirection().multiply(distance.doubleValue()*(-1d));
		return player.getLocation().add(inverseDirectionVec);
	}



}
