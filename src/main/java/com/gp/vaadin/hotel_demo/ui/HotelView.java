package com.gp.vaadin.hotel_demo.ui;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import com.gp.vaadin.hotel_demo.model.Hotel;
import com.gp.vaadin.hotel_demo.model.filter.HotelFilter;
import com.gp.vaadin.hotel_demo.service.HotelCategoryService;
import com.gp.vaadin.hotel_demo.service.HotelService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

public class HotelView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4471717033494212321L;
	
	private HotelService hotelService = HotelService.getInstance();
	private HotelCategoryService hotelCategoryService = HotelCategoryService.getInstance();
	
    private Grid<Hotel> grid = new Grid<>();
    
    private TextField nameFilter = new TextField();
    private TextField addressFilter = new TextField();
    
    private HotelForm form = new HotelForm(this);
    
    private Button addHotelBtn = new Button("Add hotel");
    private Button deleteHotelBtn = new Button("Delete hotel");
    private Button editHotelBtn = new Button("Edit hotel");
    
    public HotelView() {
    	
    	final VerticalLayout layout = new VerticalLayout();

        nameFilter.setPlaceholder("filter by name...");
        nameFilter.addValueChangeListener(e -> updateList());
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        
        addressFilter.setPlaceholder("filter by address...");
        addressFilter.addValueChangeListener(e -> updateList());
        addressFilter.setValueChangeMode(ValueChangeMode.LAZY);
        
        addHotelBtn.addClickListener(e -> {
            grid.asMultiSelect().clear();
            form.setHotel(new Hotel());
        });
        
        deleteHotelBtn.setEnabled(false);
        deleteHotelBtn.addClickListener(e -> {
        	Set<Hotel> hotelsToDelete = grid.getSelectedItems();
        	hotelService.delete(hotelsToDelete);
        	editHotelBtn.setEnabled(false);
        	deleteHotelBtn.setEnabled(false);
        	updateList();
        	form.setVisible(false);
        });
        
        editHotelBtn.setEnabled(false);
        editHotelBtn.addClickListener(e -> {
        	Hotel hotelToEdit = grid.getSelectedItems().iterator().next();
        	editHotelBtn.setEnabled(false);
        	deleteHotelBtn.setEnabled(false);
        	grid.asMultiSelect().clear();
            form.setHotel(hotelToEdit);
        });

        HorizontalLayout toolbar = new HorizontalLayout(nameFilter, addressFilter, addHotelBtn, deleteHotelBtn, editHotelBtn);
        
        grid.addColumn(Hotel::getName).setCaption("Name");
        grid.addColumn(Hotel::getAddress).setCaption("Address");
        grid.addColumn(Hotel::getRating).setCaption("Rating");
        grid.addColumn(hotel -> hotelCategoryService.getCategoryNameById(hotel.getCategoryId())).setCaption("Category");
        
        grid.addColumn(hotel -> LocalDateTime
				.ofInstant(Instant.ofEpochMilli(hotel.getOperatesFrom()), ZoneId.systemDefault()).toLocalDate()).setCaption("Operates from");
        grid.addColumn(Hotel::getDescription).setCaption("Description");
        Grid.Column<Hotel, String> urlColumn = grid.addColumn(hotel -> "<a href='" + hotel.getUrl() + "' target = '_target'>hotel info</a>", new HtmlRenderer());
        urlColumn.setCaption("Url");

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setSizeFull();
        grid.setSizeFull();
        content.setExpandRatio(grid, 1);

        layout.addComponents(toolbar, content);

        updateList();

        addComponent(layout);

        form.setVisible(false);
        
        grid.setSelectionMode(SelectionMode.MULTI);
        
        grid.addSelectionListener(event -> {
            Set<Hotel> selectedHotels = event.getAllSelectedItems();
            deleteHotelBtn.setEnabled(selectedHotels.size() > 0);
            editHotelBtn.setEnabled(selectedHotels.size() == 1);
        });

    }
    
    public void onActivate() {
    	form.onInitialize();
    	
    	updateList();
    }
    
    public void updateList() {
        List<Hotel> hotels = hotelService.findAll(new HotelFilter(nameFilter.getValue(), addressFilter.getValue()));
        grid.setItems(hotels);
    }
    
    public void onCloseEditForm() {
		deleteHotelBtn.setEnabled(false);
	}
    
    @Override
    public void enter(ViewChangeEvent event) {
    	onActivate();
    }

}
