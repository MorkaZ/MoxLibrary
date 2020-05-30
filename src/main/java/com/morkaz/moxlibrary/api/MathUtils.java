package com.morkaz.moxlibrary.api;

import org.bukkit.Location;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtils {

	public static boolean chanceOfPercent(Number percent) {
		if (percent.doubleValue() <= 0.0){
			return false;
		}
		return chanceOfDec(percent.doubleValue()/100.0);
	}

	public static boolean chanceOfDec(Number decimental) {
		if (decimental.doubleValue() <= 0.0){
			return false;
		}
		if (Math.random() < decimental.doubleValue()) {
			return true;
		}
		return false;
	}

	public static double calculateDistance2D(Location loc1, Location loc2){
		Double xDif = Math.sqrt(Math.pow(loc1.getX()-loc2.getX(), 2));
		Double zDif = Math.sqrt(Math.pow(loc1.getZ()-loc2.getZ(), 2));
		return xDif + zDif;
	}

	public static double calculateDistance3D(Location loc1, Location loc2){
		return loc1.distance(loc2);
	}

	public static Double randomDouble(double minimum, double maximum){
		Random random = new Random();
		return randomDouble(minimum, maximum, random);
	}

	public static Double randomDouble(double minimum, double maximum, Random random) {
		return random.nextDouble() * (maximum - minimum) + minimum;
	}

	public static Integer randomInteger(int minimum, int maximum, Random random){
		return random.nextInt((maximum - minimum) + 1) + minimum;
	}

	public static Integer randomInteger(int minimum, int maximum){
		Random random = new Random();
		return randomInteger(minimum, maximum, random);
	}

	public static Double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_DOWN);
		return bd.doubleValue();
	}

}
