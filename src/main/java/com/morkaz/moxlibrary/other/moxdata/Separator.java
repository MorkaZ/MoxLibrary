package com.morkaz.moxlibrary.other.moxdata;

public enum Separator {

	PAIR("%=%"),
	CHAIN("%.%"),
	MAIN_KEY("%:%");

	String separator;

	private Separator(String separator){
		this.separator = separator;
	}

	@Override
	public String toString() {
		return separator;
	}
}
