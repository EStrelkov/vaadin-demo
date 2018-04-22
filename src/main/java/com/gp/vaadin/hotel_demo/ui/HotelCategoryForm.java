package com.gp.vaadin.hotel_demo.ui;

import com.gp.vaadin.hotel_demo.model.HotelCategory;
import com.gp.vaadin.hotel_demo.service.HotelCategoryService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HotelCategoryForm extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4404488930218649725L;
	
	private TextField name = new TextField("Name");
    
    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private HotelCategoryService categoryService = HotelCategoryService.getInstance();
    
    private HotelCategory hotelCategory;
    private HotelCategoryView hotelCategoryUI;
    private Binder<HotelCategory> binder = new Binder<>(HotelCategory.class);

    public HotelCategoryForm(HotelCategoryView hotelCategoryUI) {
        this.hotelCategoryUI = hotelCategoryUI;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, close);
        
        addComponents(name, buttons);
        
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        prepareFields();

        save.addClickListener(e -> this.save());
        close.addClickListener(e -> this.close());
    }

    private void prepareFields() {
    	binder.forField(name).asRequired("Please enter a name").bind(HotelCategory::getName, HotelCategory::setName);
    	name.setDescription("Hotel category name");
	}

	public void setHotelCategory(HotelCategory hotelCategory) {
        this.hotelCategory = hotelCategory;
        
        binder.readBean(hotelCategory);

        setVisible(true);
        name.selectAll();
    }

    private void close() {
    	hotelCategoryUI.updateList();
        setVisible(false);
        hotelCategoryUI.onCloseEditForm();
    }

    private void save() {
    	
    	if (binder.isValid()) {
    		try {
    			binder.writeBean(hotelCategory);
    			categoryService.save(hotelCategory);
    		} catch (ValidationException e) {
    			Notification.show("Unable to save." + e.getMessage(), Type.HUMANIZED_MESSAGE);
    		} finally {
    			close();
    		}
    	} else {
    		Notification.show("Unable to save.", Type.HUMANIZED_MESSAGE);
    	}
        
    }

	
}
