package com.gp.vaadin.hotel_demo.ui;

public enum View {
	HOTEL("Hotel"),
	HOTEL_CATEGORY("Category");
	
	private final String name;

	private View(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
