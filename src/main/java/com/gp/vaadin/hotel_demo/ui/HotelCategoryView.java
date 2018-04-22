package com.gp.vaadin.hotel_demo.ui;

import java.util.List;
import java.util.Set;

import com.gp.vaadin.hotel_demo.model.HotelCategory;
import com.gp.vaadin.hotel_demo.model.filter.HotelCategoryFilter;
import com.gp.vaadin.hotel_demo.service.HotelCategoryService;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class HotelCategoryView extends VerticalLayout implements View {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4743969174479993938L;
	
	private HotelCategoryService hotelCategoryService = HotelCategoryService.getInstance();
    private Grid<HotelCategory> grid = new Grid<>();
    
    private TextField nameFilter = new TextField();
    
    private HotelCategoryForm form = new HotelCategoryForm(this);
    
    private Button addHotelCategoryBtn = new Button("Add category");
    private Button deleteHotelCategoryBtn = new Button("Delete category");
    private Button editHotelCategoryBtn = new Button("Edit category");
    
    public HotelCategoryView() {
    	
    	final VerticalLayout layout = new VerticalLayout();

        nameFilter.setPlaceholder("filter by name...");
        nameFilter.addValueChangeListener(e -> updateList());
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        
        addHotelCategoryBtn.addClickListener(e -> {
            grid.asMultiSelect().clear();
            form.setHotelCategory(new HotelCategory());
        });
        
        deleteHotelCategoryBtn.setEnabled(false);
        deleteHotelCategoryBtn.addClickListener(e -> {
        	Set<HotelCategory> hotelCategoriesToDelete = grid.getSelectedItems();
        	hotelCategoryService.delete(hotelCategoriesToDelete);
        	editHotelCategoryBtn.setEnabled(false);
        	deleteHotelCategoryBtn.setEnabled(false);
        	updateList();
        	form.setVisible(false);
        });
        
        editHotelCategoryBtn.setEnabled(false);
        editHotelCategoryBtn.addClickListener(e -> {
        	HotelCategory hotelCategoryToEdit = grid.getSelectedItems().iterator().next();
        	editHotelCategoryBtn.setEnabled(false);
        	deleteHotelCategoryBtn.setEnabled(false);
        	grid.asMultiSelect().clear();
            form.setHotelCategory(hotelCategoryToEdit);
        });

        HorizontalLayout toolbar = new HorizontalLayout(nameFilter, addHotelCategoryBtn, deleteHotelCategoryBtn, editHotelCategoryBtn);
        
        grid.addColumn(HotelCategory::getName).setCaption("Name");

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
            Set<HotelCategory> selectedHotelCategories = event.getAllSelectedItems();
            deleteHotelCategoryBtn.setEnabled(selectedHotelCategories.size() > 0);
            editHotelCategoryBtn.setEnabled(selectedHotelCategories.size() == 1);
        });

    }
    
    public void updateList() {
        List<HotelCategory> hotelCategories = hotelCategoryService.findAll(new HotelCategoryFilter(nameFilter.getValue()));
        grid.setItems(hotelCategories);
    }
    
    public void onCloseEditForm() {
    	deleteHotelCategoryBtn.setEnabled(false);
	}

}
