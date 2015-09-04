package com.aquamorph.ecubustracker.Models;

public class RouteData {

	private String routeTitle = "routeTitle";
	private String routeTag = "routeTag";
	private String stopTitle = "stopTitle";
	private String stopTag = "stopTag";

	public RouteData(String routeTitle, String routeTag, String stopTitle, String stopTag) {
		this.routeTitle = routeTitle;
		this.routeTag = routeTag;
		this.stopTitle = stopTitle;
		this.stopTag = stopTag;
	}

	public String getRouteTitle() {
		return routeTitle;
	}

	public String getRouteTag() {
		return routeTag;
	}

	public String getStopTitle() {
		return stopTitle;
	}

	public String getStopTag() {
		return stopTag;
	}
}
