package com.gp.vaadin.hotel_demo.model.filter;

public class HotelFilter {

	private final String name;
	
	private final String address;

	public HotelFilter(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
}
