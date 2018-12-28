package com.morkaz.moxlibrary.api;

import org.bukkit.Location;

import java.util.Random;

public class MathUtils {


	public static boolean chanceOfPercent(Number procent) {
		return chanceOfDec(procent.doubleValue()/100d);
	}

	public static boolean chanceOfDec(Number decimental) {
		Double realNumber = decimental.doubleValue();
		Random random = new Random();
		Double randomNumber = random.nextDouble();
		return (randomNumber <= realNumber);
	}

	public static double calculateDistance2D(Location loc1, Location loc2){
		Double xDif = Math.sqrt(Math.pow(loc1.getX()-loc2.getX(), 2));
		Double zDif = Math.sqrt(Math.pow(loc1.getZ()-loc2.getZ(), 2));
		return xDif + zDif;
	}

	public static double calculateDistance3D(Location loc1, Location loc2){
		return loc1.distance(loc2);
	}

}
