package com.gp.vaadin.hotel_demo.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Hotel implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5028515958056807912L;

	private Long id;

	private String name = "";

	private String address = "";

	private String rating;

	private LocalDate operatesFrom;

	private HotelCategory category;
	
	private String url;
	
	private String description;

	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public String toString() {
		return name + " " + rating +"stars " + address;
	}

	@Override
	public Hotel clone() {
		try {
			return (Hotel) super.clone();
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public LocalDate getOperatesFrom() {
		return operatesFrom;
	}

	public void setOperatesFrom(LocalDate operatesFrom) {
		this.operatesFrom = operatesFrom;
	}

	public HotelCategory getCategory() {
		return category;
	}

	public void setCategory(HotelCategory category) {
		this.category = category;
	}	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}