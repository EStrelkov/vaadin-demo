package com.gp.vaadin.hotel_demo.model.filter;

public class HotelCategoryFilter implements Filter {

	private final String name;

	public HotelCategoryFilter(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
