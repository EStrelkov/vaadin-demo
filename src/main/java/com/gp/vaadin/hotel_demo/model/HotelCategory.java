package com.gp.vaadin.hotel_demo.model;

public class HotelCategory implements IdentifiedObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1995198874883280710L;

	private Long id;

	private String name = "";
	
	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public HotelCategory clone() {
		try {
			return (HotelCategory) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static HotelCategory empty() {
		HotelCategory hotelCategory = new HotelCategory();
		hotelCategory.setName("No Category");
		return hotelCategory;
	}
}
