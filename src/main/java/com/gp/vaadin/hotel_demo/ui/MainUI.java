package com.gp.vaadin.hotel_demo.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("mytheme")
public class MainUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5797851854545091816L;

	@Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);
        
        Panel viewDisplay = new Panel();
        viewDisplay.setSizeFull();

        Navigator navigator = new Navigator(this, viewDisplay);
        navigator.addView(View.HOTEL.getName(), new HotelView());
        navigator.addView(View.HOTEL_CATEGORY.getName(), new HotelCategoryView());
        navigator.navigateTo(View.HOTEL.getName());

        NavigableMenuBar menu = new NavigableMenuBar(navigator);
        menu.addStyleName("mybarmenu");
        layout.addComponent(menu);
        
        layout.addComponent(viewDisplay);
        layout.setExpandRatio(viewDisplay, 1.0f);

        navigator.addViewChangeListener(menu);
        
        menu.addView(View.HOTEL.getName(), View.HOTEL.getName(), VaadinIcons.BUILDING);
        menu.addView(View.HOTEL_CATEGORY.getName(), View.HOTEL_CATEGORY.getName(), VaadinIcons.ACADEMY_CAP);
        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
    }
	
	@WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7277889898805004773L;
    }
	
}
