package com.gp.vaadin.hotel_demo.ui;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.gp.vaadin.hotel_demo.model.Hotel;
import com.gp.vaadin.hotel_demo.model.filter.HotelFilter;
import com.gp.vaadin.hotel_demo.service.HotelService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ComponentRenderer;


@Theme("mytheme")
public class HotelUI extends UI {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6238115301728540960L;
	
	private HotelService hotelService = HotelService.getInstance();
    private Grid<Hotel> grid = new Grid<>(Hotel.class);
    
    private TextField nameFilter = new TextField();
    private TextField addressFilter = new TextField();
    
    private HotelForm form = new HotelForm(this);
    
    private Button addHotelBtn = new Button("Add hotel");
    private Button deleteHotelBtn = new Button("Delete hotel");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        nameFilter.setPlaceholder("filter by name...");
        nameFilter.addValueChangeListener(e -> updateList());
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        
        addressFilter.setPlaceholder("filter by address...");
        addressFilter.addValueChangeListener(e -> updateList());
        addressFilter.setValueChangeMode(ValueChangeMode.LAZY);

        addHotelBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setHotel(new Hotel());
        });
        
        deleteHotelBtn.setEnabled(false);
        deleteHotelBtn.addClickListener(e -> {
        	Hotel hotelToDelete = grid.getSelectedItems().iterator().next();
        	hotelService.delete(hotelToDelete);
        	deleteHotelBtn.setEnabled(false);
        	updateList();
        	form.setVisible(false);
        });

        HorizontalLayout toolbar = new HorizontalLayout(nameFilter, addressFilter, addHotelBtn, deleteHotelBtn);

        grid.setColumns("name", "address", "rating", "category");
        
        grid.addColumn(
        	    hotel -> createHotelLink(hotel) ,
        	    new ComponentRenderer()
        	).setCaption( "Url" );

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setSizeFull();
        grid.setSizeFull();
        content.setExpandRatio(grid, 1);

        layout.addComponents(toolbar, content);

        updateList();

        setContent(layout);

        form.setVisible(false);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                form.setVisible(false);
            } else {
                form.setHotel(event.getValue());
                deleteHotelBtn.setEnabled(true);
            }
        });
    }

	private Link createHotelLink(Hotel hotel) {
		Link link = new Link(hotel.getUrl(), new ExternalResource(hotel.getUrl()));
		link.setTargetName("_blank");
		link.addStyleName("icon-after-caption");
		return link;
	}

    public void updateList() {
        List<Hotel> hotels = hotelService.findAll(new HotelFilter(nameFilter.getValue(), addressFilter.getValue()));
        grid.setItems(hotels);
    }

    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class HotelUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7277889898805004773L;
    }
}
