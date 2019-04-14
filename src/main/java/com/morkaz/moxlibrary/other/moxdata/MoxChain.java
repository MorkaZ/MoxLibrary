package com.morkaz.moxlibrary.other.moxdata;

import java.util.*;

public class MoxChain {

	Map<String, MoxPair> pairMap = new LinkedHashMap<>(); // Linked to save collection order

	public MoxChain(Collection<MoxPair> pairList){
		this.addPairs(pairList);
	}

	public MoxChain(MoxPair... pairs){
		for (MoxPair pair : pairs){
			pairMap.put(pair.getKey(), pair);
		}
	}

	public MoxChain(String stringData){
		String[] splitedStringData = stringData.split(Separator.CHAIN.toString().replace(".", "\\."));
		List<MoxPair> pairList = new ArrayList<>();
		for (String stringPair : splitedStringData){
			MoxPair pair = null;
			try {
				if (stringPair.contains(Separator.PAIR.toString())){
					pair = new MoxPair(stringPair);
				} else {
					// Continue because it will not be pair so this part of chain should be discarded.
					continue;
				}
			} catch (UncorrectStringDataException e) {
				e.printStackTrace();
			}
			if (pair != null){
				pairList.add(pair);
			}
		}
		this.addPairs(pairList);
	}

	public List<String> getKeys(){
		List<String> keyList = new ArrayList<>();
		for(MoxPair moxPair : getPairs()){
			keyList.add(moxPair.getKey());
		}
		return keyList;
	}

	public List<Object> getValues(){
		List<Object> valueList = new ArrayList<>();
		for(MoxPair moxPair : getPairs()){
			valueList.add(moxPair.getValue());
		}
		return valueList;
	}

	public void clear(){
		this.pairMap.clear();
	}

	public MoxPair getPair(String key){
		return this.pairMap.get(key);
	}

	public Collection<MoxPair> getPairs(){
		return this.pairMap.values();
	}

	public void addPair(MoxPair pair){
		if (pair == null){
			return;
		}
		this.pairMap.put(pair.getKey(), pair);
	}

	public void addPairs(Collection<MoxPair> pairs){
		for (MoxPair pair : pairs){
			this.pairMap.put(pair.getKey(), pair);
		}
	}

	public boolean removePair(String key){
		return this.pairMap.remove(key) != null ? true : false;
	}

	public boolean removePair(MoxPair pair){
		return this.pairMap.remove(pair.getKey()) != null ? true : false;
	}

	public Boolean isEmpty(){
		return this.pairMap.isEmpty();
	}

	public String getString(){
		return this.toString();
	}

	@Override
	public String toString(){
		String stringData = "";
		boolean first = true;
		for (Map.Entry<String, MoxPair> entry : pairMap.entrySet()){
			MoxPair pair = entry.getValue();
			if (first){
				first = false;
				stringData = pair.toString();
				continue;
			}
			stringData = stringData + Separator.CHAIN + pair.toString();
		}
		return stringData;
	}

}
