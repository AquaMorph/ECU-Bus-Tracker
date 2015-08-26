package com.aquamorph.ecubustracker.Models;

public class Predictions {
	public int seconds;
	public int minutes;
	public Boolean isDeparture;
	public Boolean affectedByLayover;
	public String dirTag;
	public int vehicle;
	public int block;

	Predictions(int seconds, int minutes, Boolean isDeparture, Boolean affectedByLayover,
	            String dirTag, int vehicle, int block) {
		this.seconds = seconds;
		this.minutes = minutes;
		this.isDeparture = isDeparture;
		this.affectedByLayover = affectedByLayover;
		this.dirTag = dirTag;
		this.vehicle = vehicle;
		this.block = block;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getBlock() {
		return block;
	}

	public int getMinutes() {
		return minutes;
	}

	public Boolean getIsDeparture() {
		return isDeparture;
	}

	public Boolean getAffectedByLayover() {
		return affectedByLayover;
	}

	public String getDirTag() {
		return dirTag;
	}

	public int getVehicle() {
		return vehicle;
	}
}
