package com.gp.vaadin.hotel_demo.service;

import java.util.Random;

import com.gp.vaadin.hotel_demo.model.HotelCategory;
import com.gp.vaadin.hotel_demo.model.filter.HotelCategoryFilter;

public class HotelCategoryService extends AbstractTestDataService<HotelCategory, HotelCategoryFilter> {

	private HotelCategoryService() {
	}

	private static class HotelCategoryServiceHolder {
		private static final HotelCategoryService INSTANCE = createHotelCategoryService();

		private static HotelCategoryService createHotelCategoryService() {
			HotelCategoryService hotelCategoryService = new HotelCategoryService();
			hotelCategoryService.ensureTestData();
			return hotelCategoryService;
		}
	}

	public static HotelCategoryService getInstance() {
		return HotelCategoryServiceHolder.INSTANCE;
	}
	
	@Override
	protected HotelCategory copy(HotelCategory hotelCategory) {
		return hotelCategory.clone();
	}

	@Override
	protected boolean isPassesFilter(HotelCategory hotelCategory, HotelCategoryFilter hotelCategoryFilter) {
		if (hotelCategoryFilter == null) {
			return true;
		}
		
		return isContainsSubstring(hotelCategory.getName(), hotelCategoryFilter.getName());
	}

	@Override
	protected HotelCategory createTestObject(String[] split, Random r) {
		HotelCategory h = new HotelCategory();
		h.setName(split[0]);
		return h;
	}

	@Override
	protected String[] getTestData() {
		return new String[] {"Hotel", "Hostel", "GuestHouse", "Appartments"};
	}

	public String getCategoryNameById(Long id) {
		HotelCategory hotelCategory = getById(id);
		return hotelCategory == null ? "No Category" : hotelCategory.getName();
	}

}
