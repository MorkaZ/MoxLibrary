package com.morkaz.moxlibrary.misc;

import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleUnixPurger {

	private Plugin plugin;
	private Map<Object, Long> objectMap = new HashMap<>();

	public SimpleUnixPurger(Plugin plugin) {
		this.plugin = plugin;
	}

	public SimpleUnixPurger(Plugin plugin, Map<Object, Long> objectMap){
		this.plugin = plugin;
		this.objectMap = objectMap;
	}

	public void setObjectMap(Map<Object, Long> objectMap){
		this.objectMap = objectMap;
	}

	public void addObjectsToMap(Map<Object, Long> objectMap){
		this.objectMap.putAll(objectMap);
	}

	public void addObjectToMap(Object keyObject, Long unixDate){
		this.objectMap.put(keyObject, unixDate);
	}

	public void removeObjectFromMap(Object objectKey){
		this.objectMap.remove(objectKey);
	}

	public void removeObjectsFromMap(Map<Object, Long> objectMap){
		this.objectMap.keySet().removeAll(objectMap.entrySet());
	}

	public void removeObjectsFromMap(Collection<Object> objects){
		this.objectMap.keySet().removeAll(objects);
	}

	public Map<Object, Long> getToPurgeObjects(Long minDate){
		Map<Object, Long> objectMap2 = new HashMap<>();
		for (Map.Entry<Object, Long> entry : objectMap.entrySet()){
			Object objectKey = entry.getKey();
			Long objectDate = entry.getValue();
			if (objectDate == null || minDate > objectDate){
				objectMap2.put(objectKey, objectDate);
				continue;
			}
		}
		return objectMap2;
	}

	public Map<Object, Long> getNotToPurgeObjects(Long minDate){
		Map<Object, Long> notToPurgeMap = new HashMap<>(objectMap);
		notToPurgeMap.keySet().removeAll(this.getToPurgeObjects(minDate).keySet());
		return notToPurgeMap;
	}

}
