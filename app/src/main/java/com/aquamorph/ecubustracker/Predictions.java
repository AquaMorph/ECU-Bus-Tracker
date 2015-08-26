package com.aquamorph.ecubustracker;

public class Predictions {
	public int seconds;
	public int minutes;
	public Boolean isDeparture;
	public Boolean affectedByLayover;
	public String dirTag;
	public int vehicle;
	public int block;

	Predictions(int seconds) {
		this.seconds = seconds;
	}

	public int getSeconds() {
		return seconds;
	}
}
