package com.gp.vaadin.hotel_demo.model;

import java.io.Serializable;

public interface IdentifiedObject extends Serializable, Cloneable {

	Long getId();
	
	void setId(Long id);
	
}
