package com.morkaz.moxlibrary.other;

import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleUnixPurger<T> {

	private Plugin plugin;
	private Map<T, Long> objectMap = new HashMap<>();

	public SimpleUnixPurger(Plugin plugin) {
		this.plugin = plugin;
	}

	public SimpleUnixPurger(Plugin plugin, Map<T, Long> objectMap){
		this.plugin = plugin;
		this.objectMap = objectMap;
	}

	public void setObjectMap(Map<T, Long> objectMap){
		this.objectMap = objectMap;
	}

	public void addObjectsToMap(Map<T, Long> objectMap){
		this.objectMap.putAll(objectMap);
	}

	public void addObjectToMap(T keyObject, Long unixDate){
		this.objectMap.put(keyObject, unixDate);
	}

	public void removeObjectFromMap(T objectKey){
		this.objectMap.remove(objectKey);
	}

	public void removeObjectsFromMap(Map<T, Long> objectMap){
		this.objectMap.keySet().removeAll(objectMap.entrySet());
	}

	public void removeObjectsFromMap(Collection<T> objects){
		this.objectMap.keySet().removeAll(objects);
	}

	public Map<Object, Long> getToPurgeObjects(Long minDate){
		Map<Object, Long> filtredMap = new HashMap<>();
		for (Map.Entry<T, Long> entry : objectMap.entrySet()){
			Object objectKey = entry.getKey();
			Long objectDate = entry.getValue();
			if (objectDate == null || minDate > objectDate){
				filtredMap.put(objectKey, objectDate);
				continue;
			}
		}
		return filtredMap;
	}

	public Map<T, Long> getNotToPurgeObjects(Long minDate){
		Map<T, Long> notToPurgeMap = new HashMap<>(objectMap);
		notToPurgeMap.keySet().removeAll(this.getToPurgeObjects(minDate).keySet());
		return notToPurgeMap;
	}

}
