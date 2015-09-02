package com.aquamorph.ecubustracker.Models;

public class Routes {
	private String tag;
	private String title;

	public Routes(String tag,String title) {
		this.tag = tag;
		this.title = title;
	}

	public String getTag() {
		return tag;
	}

	public String getTitle() {
		return title;
	}
}
