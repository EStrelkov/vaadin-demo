package com.gp.vaadin.hotel_demo.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;

public class NavigableMenuBar extends MenuBar implements ViewChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5875788005361397445L;
	
	private MenuItem previous = null;
    private MenuItem current  = null;

    private Map<String, MenuItem> menuItems = new HashMap<String, MenuItem>();
    
    private Navigator navigator = null;
    
    public NavigableMenuBar(Navigator navigator) {
        this.navigator = navigator;
    }
    
    private MenuBar.Command mycommand = new MenuBar.Command() {

        /**
		 * 
		 */
		private static final long serialVersionUID = -1940083128505774912L;

		public void menuSelected(MenuItem selectedItem) {
            String viewName = selectItem(selectedItem);
            navigator.navigateTo(viewName);
        }
    };
    
    public void addView(String viewName, String caption, Resource icon) {
        menuItems.put(viewName, addItem(caption, icon, mycommand));
    }

    private boolean selectView(String viewName) {
        if (!menuItems.containsKey(viewName))
            return false;

        if (previous != null) {
            previous.setStyleName(null);
        }
        if (current == null) {
            current = menuItems.get(viewName);
        }
        current.setStyleName("highlight");
        previous = current;
        
        return true;
    }

    public String selectItem(MenuItem selectedItem) {
        current = selectedItem;
        
        for (Entry<String,MenuItem> entry : menuItems.entrySet()) {
        	if (entry.getValue() == selectedItem) {
        		return entry.getKey();
        	}
        }
        
        return null;
    }

    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        return selectView(event.getViewName());
    }
        
    @Override
    public void afterViewChange(ViewChangeEvent event) {}
	
}
