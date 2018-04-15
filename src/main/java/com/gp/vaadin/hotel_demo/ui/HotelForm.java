package com.gp.vaadin.hotel_demo.ui;

import com.gp.vaadin.hotel_demo.model.Hotel;
import com.gp.vaadin.hotel_demo.model.HotelCategory;
import com.gp.vaadin.hotel_demo.service.HotelService;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HotelForm extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4404488930218649725L;
	
	private TextField name = new TextField("Name");
    private TextField address = new TextField("Address");
    private TextField rating = new TextField("Rating");
    private DateField operatesFrom = new DateField("Operates from");
    private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");
    private TextField url = new TextField("Url");
    private TextArea description = new TextArea("Description");
    
    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private HotelService service = HotelService.getInstance();
    private Hotel hotel;
    private HotelUI hotelUI;
    private Binder<Hotel> binder = new Binder<>(Hotel.class);

    public HotelForm(HotelUI hotelUI) {
        this.hotelUI = hotelUI;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, close);
        addComponents(name, address, rating, category, operatesFrom, url, description, buttons);

        category.setItems(HotelCategory.values());
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        binder.bindInstanceFields(this);

        save.addClickListener(e -> this.save());
        close.addClickListener(e -> this.close());
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        binder.setBean(hotel);

        setVisible(true);
        name.selectAll();
    }

    private void close() {
        setVisible(false);
    }

    private void save() {
        service.save(hotel);
        hotelUI.updateList();
        setVisible(false);
    }

	
}
