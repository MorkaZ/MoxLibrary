package com.morkaz.moxlibrary.api;


import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MySQLResultSetUtils {


	public static HashMap<Object, Object> getValuedMapFromSet(String asKey, String asValue, ResultSet set){
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		try {
			while(set.next()) {
				map.put(set.getObject(asKey), set.getObject(asValue));
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static LinkedHashMap<Object, Object> getValuedLinkedMapFromSet(String asKey, String asValue, ResultSet set){
		LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object>();
		try {
			while(set.next()) {
				map.put(set.getObject(asKey), set.getObject(asValue));
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}


	public static List<Object> getObjectsFromSet(String columnName, ResultSet set){
		List<Object> list = new ArrayList<Object>();
		try {
			while(set.next()) {
				Object value = set.getObject(columnName);
				if (set.wasNull()){
					continue;
				}
				list.add(value);
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}


	public static Object getObjectFromSet(String columnName, ResultSet set){
		Object value = null;
		try {
			while(set.next()) {
				value = set.getObject(columnName);
				if (set.wasNull()){
					value = null;
				}
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static Integer getIntegerFromSet(String columnName, ResultSet set){
		Integer value = null;
		try {
			while(set.next()) {
				value = set.getInt(columnName);
				if (set.wasNull()){
					value = null;
				}
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static List<Integer> getIntegersFromSet(String columnName, ResultSet set){
		List<Integer> list = new ArrayList<Integer>();
		try {
			while(set.next()) {
				Integer value = set.getInt(columnName);
				if (set.wasNull()){
					continue;
				}
				list.add(value);
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static Long getLongFromSet(String columnName, ResultSet set){
		Long value = null;
		try {
			while(set.next()) {
				value = set.getLong(columnName);
				if (set.wasNull()){
					value = null;
				}
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static List<Long> getLongsFromSet(String columnName, ResultSet set){
		List<Long> list = new ArrayList<Long>();
		try {
			while(set.next()) {
				Long value = set.getLong(columnName);
				if (set.wasNull()){
					continue;
				}
				list.add(value);
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}


	public static Double getDoubleFromSet(String columnName, ResultSet set){
		Double value = null;
		try {
			while(set.next()) {
				value = set.getDouble(columnName);
				if (set.wasNull()){
					value = null;
				}
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static List<Double> getDoublesFromSet(String columnName, ResultSet set){
		List<Double> list = new ArrayList<Double>();
		try {
			while(set.next()) {
				Double value = set.getDouble(columnName);
				if (set.wasNull()){
					continue;
				}
				list.add(value);
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}


	public static Short getShortFromSet(String columnName, ResultSet set){
		Short value = null;
		try {
			while(set.next()) {
				value = set.getShort(columnName);
				if (set.wasNull()){
					value = null;
				}
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static List<Short> getShortsFromSet(String columnName, ResultSet set){
		List<Short> list = new ArrayList<Short>();
		try {
			while(set.next()) {
				Short value = set.getShort(columnName);
				if (set.wasNull()){
					continue;
				}
				list.add(value);
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}


	@Nullable
	public static String getStringFromSet(String columnName, ResultSet set){
		String name = null;
		try {
			while(set.next()) {
				name = set.getString(columnName);
			}
			set.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}


	public static ArrayList<String> getStringsFromSet(String columnName, ResultSet set){
		ArrayList<String> list = new ArrayList<String>();
		try {
			while(set.next()) {
				String name = set.getString(columnName);
				list.add(name);
			}
			set.beforeFirst();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return list;
	}
}
