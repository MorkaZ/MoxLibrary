package com.morkaz.moxlibrary.other.moxdata;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class MoxPair<T> {

	Pair<String, T> pair;

	public MoxPair(String key, T value){
		this.pair = new MutablePair(key, value);
	}

	/*
		<p>Generic T may be only String in this constructor. In other cases errors will be thrown.</p>
	 */
	public MoxPair(String stringData) throws UncorrectStringDataException{
		String[] splitedStringData = stringData.split(Separator.PAIR.toString());
		if (splitedStringData.length != 2) {
			throw new UncorrectStringDataException("Data has not been splited correctly! Splited string data must have 1 separator. " +
					"Actual separators amount: " + splitedStringData.length + ". " +
					"Splited data: " + String.join("", splitedStringData));
		}
		this.pair = new MutablePair(splitedStringData[0], (T)splitedStringData[1]);
	}

	public T getValue(){
		return this.pair.getValue();
	}

	public void setValue(T value){
		this.pair.setValue(value);
	}

	public String getKey(){
		return this.pair.getKey();
	}

	public String getString(){
		return this.toString();
	}

	@Override
	public String toString(){
		return pair.getLeft() + Separator.PAIR + pair.getRight();
	}

}
