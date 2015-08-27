package com.aquamorph.ecubustracker.Models;

public class Stops {
	private String tag;
	private String title;
	private double lat;
	private double lon;
	private String stopId;

	public Stops(String tag, String title, double lat, double lon, String stopId) {
		this.tag = tag;
		this.title = title;
		this.lat = lat;
		this.lon = lon;
		this.stopId = stopId;
	}

	public String getTag() {
		return tag;
	}

	public String getTitle() {
		return title;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String getStopId() {
		return stopId;
	}
}
