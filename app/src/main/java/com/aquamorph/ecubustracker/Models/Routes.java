package com.aquamorph.ecubustracker.Models;

public class Routes {
	private int tag;
	private String title;

	public Routes(int tag,String title) {
		this.tag = tag;
		this.title = title;
	}

	public int getTag() {
		return tag;
	}

	public String getTitle() {
		return title;
	}
}
