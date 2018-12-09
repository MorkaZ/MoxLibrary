package com.morkaz.moxlibrary.other.moxdata;

import javax.annotation.Nullable;

public class MoxData {

	private MoxChain chain;
	private String mainKey;

	public MoxData(String mainKey, MoxChain chain){
		this.mainKey = mainKey;
		this.chain = chain;
	}

	public MoxData(MoxChain chain){
		this.chain = chain;
	}

	public MoxData(String stringData){
		String mainKey = null;
		MoxChain chain;
		if (stringData.contains(Separator.MAIN_KEY.toString())){
			String[] splitedStringData = stringData.split(Separator.MAIN_KEY.toString());
			mainKey = splitedStringData[0];
			chain = new MoxChain(splitedStringData[1]);
		} else {
			chain = new MoxChain(stringData);
		}
		this.chain = chain;
		if (mainKey != null){
			this.mainKey = mainKey;
		}
	}

	public MoxChain getChain(){
		return this.chain;
	}

	public void addPair(MoxPair pair){
		if (chain == null){
			this.initializeEmptyChain();
		}
		this.chain.addPair(pair);
	}

	public void add(String key, Object value){
		MoxPair pair = new MoxPair(key, value);
		this.addPair(pair);
	}

	public boolean set(String key, Object value){
		if (chain == null){
			this.initializeEmptyChain();
			return false;
		}
		MoxPair pair = this.chain.getPair(key);
		if (pair == null){
			this.add(key, value);
		} else {
			pair.setValue(value);
		}
		return true;
	}

	@Nullable
	public MoxPair getPair(String key){
		if (chain == null){
			this.initializeEmptyChain();
		}
		return this.chain.getPair(key);
	}

	@Nullable
	public Object get(String key){
		MoxPair pair = this.getPair(key);
		if (pair != null){
			return pair.getValue();
		}
		return null;
	}

	public boolean remove(String key){
		if (chain == null){
			this.initializeEmptyChain();
			return false;
		} else if (chain.isEmpty()){
			return false;
		}
		return chain.removePair(key);
	}

	@Nullable
	public Number getNumber(String key){
		Object value = this.get(key);
		if (value instanceof Number){
			return (Number)value;
		}
		return null;
	}

	@Nullable
	public String getString(String key){
		Object value = this.get(key);
		if (value instanceof String){
			return (String)value;
		}
		return null;
	}

	@Nullable
	public String getMainKey(){
		return this.mainKey;
	}

	@Override
	public String toString(){
		String stringData = "";
		if (chain == null){
			this.initializeEmptyChain();
		}
		if (mainKey != null){
			stringData = mainKey + Separator.MAIN_KEY + chain.toString();
		} else {
			stringData = chain.toString();
		}
		return stringData;
	}

	private void initializeEmptyChain(){
		this.chain = new MoxChain();
	}


}