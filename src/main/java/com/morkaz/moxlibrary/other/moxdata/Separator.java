package com.morkaz.moxlibrary.other.moxdata;

public enum Separator {

	PAIR("‗"),
	CHAIN("…"),
	MAIN_KEY("‖");

	String symbol;

	private Separator(String symbol){
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}
}
