package com.morkaz.moxlibrary.api;


import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ToolBox {


	public ToolBox(){

	}

	public static List<String> toStringList(List<Object> objectList){
		return objectList.stream()
				.map(object -> Objects.toString(object, null))
				.collect(Collectors.toList());
	}

	public static List<String> toStringList(Object... objects){
		return toStringList(Arrays.asList(objects));
	}

	// 1y,1M,1w,1d,1h,1m,1s
	public static Integer timeExpressionToSeconds(String expression, String separator){
		if (separator == null || separator.equals("")){
			separator = ",";
		}
		String[] splitedExpression = expression.split(separator);
		Integer seconds = 0;
		for (String part : splitedExpression){
			if (part.substring(part.length()).equals("y")){
				Integer number = Integer.valueOf(part.substring(part.length()-1));
				seconds = seconds + (number * 31556926);
			} else if (part.substring(part.length()).equals("M")){
				Integer number = Integer.valueOf(part.substring(part.length()-2));
				seconds = seconds + (number * 2629743);
			} else if (part.substring(part.length()).equals("w")){
				Integer number = Integer.valueOf(part.substring(part.length()-1));
				seconds = seconds + (number * 604800);
			} else if (part.substring(part.length()).equals("d")){
				Integer number = Integer.valueOf(part.substring(part.length()-1));
				seconds = seconds + (number * 86400);
			} else if (part.substring(part.length()).equals("h")){
				Integer number = Integer.valueOf(part.substring(part.length()-1));
				seconds = seconds + (number * 3600);
			} else if (part.substring(part.length()).equals("m")){
				Integer number = Integer.valueOf(part.substring(part.length()-1));
				seconds = seconds + (number * 60);
			} else if (part.substring(part.length()).equals("s")){
				Integer number = Integer.valueOf(part.substring(part.length()-1));
				seconds = seconds + (number * 60);
			}
		}
		return seconds;
	}

	public static <C> C[] asArray(C... obj) {
		return obj;
	}

	public static <E extends Enum<E>> Boolean enumContains(Class<E> clazz, String valueString){
		E[] enumValues = clazz.getEnumConstants();
		List<String> enumStringValuesList = toStringList(enumValues);
		return enumStringValuesList.contains(valueString);
	}


}
