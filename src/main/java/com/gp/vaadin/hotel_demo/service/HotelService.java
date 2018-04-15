package com.gp.vaadin.hotel_demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gp.vaadin.hotel_demo.model.Hotel;
import com.gp.vaadin.hotel_demo.model.HotelCategory;
import com.gp.vaadin.hotel_demo.model.filter.HotelFilter;

public class HotelService {

	private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());

	private final ConcurrentMap<Long, Hotel> hotels = new ConcurrentHashMap<>();
	private AtomicLong nextId = new AtomicLong(1);

	private HotelService() {
	}

	private static class HotelServiceHolder {
		private static final HotelService INSTANCE = createHotelService();

		private static HotelService createHotelService() {
			HotelService hotelService = new HotelService();
			hotelService.ensureTestData();
			return hotelService;
		}
	}

	public static HotelService getInstance() {
		return HotelServiceHolder.INSTANCE;
	}

	public List<Hotel> findAll() {
		return findAll(null);
	}

	public List<Hotel> findAll(HotelFilter hotelFilter) {
		List<Hotel> filteredHotels = new ArrayList<>();
		for (Hotel hotel : hotels.values()) {
			if (isPassesFilter(hotel, hotelFilter)) {
				filteredHotels.add(hotel.clone());
			}
		}
		Collections.sort(filteredHotels, new HotelByIdComparator());
		return filteredHotels;
	}

	private boolean isPassesFilter(Hotel hotel, HotelFilter hotelFilter) {
		if (hotelFilter == null) {
			return true;
		}
		
		return isContainsSubstring(hotel.getName(), hotelFilter.getName()) && isContainsSubstring(hotel.getAddress(), hotelFilter.getAddress());
		
	}
	
	private boolean isContainsSubstring(String text, String pattern) {
		return pattern == null || pattern.isEmpty() || text.toLowerCase().contains(pattern.toLowerCase());
	}
	
	public List<Hotel> findAll(HotelFilter hotelFilter, int start, int maxresults) {
		List<Hotel> filteredHotels = new ArrayList<>();
		for (Hotel hotel : hotels.values()) {
			if (isPassesFilter(hotel, hotelFilter)) {
				filteredHotels.add(hotel.clone());
			}
		}
		Collections.sort(filteredHotels, new HotelByIdComparator());
		int end = start + maxresults;
		if (end > filteredHotels.size()) {
			end = filteredHotels.size();
		}
		return filteredHotels.subList(start, end);
	}

	public long count() {
		return hotels.size();
	}

	public void delete(Hotel value) {
		hotels.remove(value.getId());
	}

	public void save(Hotel entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Hotel is null.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId.getAndIncrement());
		}
		try {
			entry = entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		hotels.put(entry.getId(), entry);
	}

	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] hotelData = new String[] {
					"3 Nagas Luang Prabang - MGallery by Sofitel;4;https://www.booking.com/hotel/la/3-nagas-luang-prabang-by-accor.en-gb.html;Vat Nong Village, Sakkaline Road, Democratic Republic Lao, 06000 Luang Prabang, Laos;",
					"Abby Boutique Guesthouse;1;https://www.booking.com/hotel/la/abby-boutique-guesthouse.en-gb.html;Ban Sawang , 01000 Vang Vieng, Laos",
					"Bountheung Guesthouse;1;https://www.booking.com/hotel/la/bountheung-guesthouse.en-gb.html;Ban Tha Heua, 01000 Vang Vieng, Laos",
					"Chalouvanh Hotel;2;https://www.booking.com/hotel/la/chalouvanh.en-gb.html;13 road, Ban Phonesavanh, Pakse District, 01000 Pakse, Laos",
					"Chaluenxay Villa;3;https://www.booking.com/hotel/la/chaluenxay-villa.en-gb.html;Sakkarin Road Ban Xienthong Luang Prabang Laos, 06000 Luang Prabang, Laos",
					"Dream Home Hostel 1;1;https://www.booking.com/hotel/la/getaway-backpackers-hostel.en-gb.html;049 Sihome Road, Ban Sihome, 01000 Vientiane, Laos",
					"Inpeng Hotel and Resort;2;https://www.booking.com/hotel/la/inpeng-and-resort.en-gb.html;406 T4 Road, Donekoy Village, Sisattanak District, 01000 Vientiane, Laos",
					"Jammee Guesthouse II;2;https://www.booking.com/hotel/la/jammee-guesthouse-vang-vieng1.en-gb.html;Vang Vieng, 01000 Vang Vieng, Laos",
					"Khemngum Guesthouse 3;2;https://www.booking.com/hotel/la/khemngum-guesthouse-3.en-gb.html;Ban Thalat No.10 Road Namngum Laos, 01000 Thalat, Laos",
					"Khongview Guesthouse;1;https://www.booking.com/hotel/la/khongview-guesthouse.en-gb.html;Ban Klang Khong, Khong District, 01000 Muang Không, Laos",
					"Kong Kham Pheng Guesthouse;1;https://www.booking.com/hotel/la/kong-kham-pheng-guesthouse.en-gb.html;Mixay Village, Paksan district, Bolikhamxay province, 01000 Muang Pakxan, Laos",
					"Laos Haven Hotel & Spa;3;https://www.booking.com/hotel/la/laos-haven.en-gb.html;047 Ban Viengkeo, Vang Vieng , 01000 Vang Vieng, Laos",
					"Lerdkeo Sunset Guesthouse;1;https://www.booking.com/hotel/la/lerdkeo-sunset-guesthouse.en-gb.html;Muang Ngoi Neua,Ban Ngoy-Nua, 01000 Muang Ngoy, Laos",
					"Luangprabang River Lodge Boutique 1;3;https://www.booking.com/hotel/la/luangprabang-river-lodge.en-gb.html;Mekong River Road, 06000 Luang Prabang, Laos",
					"Manichan Guesthouse;2;https://www.booking.com/hotel/la/manichan-guesthouse.en-gb.html;Ban Pakham Unit 4/143, 60000 Luang Prabang, Laos",
					"Mixok Inn;2;https://www.booking.com/hotel/la/mixok-inn.en-gb.html;188 Sethathirate Road , Mixay Village , Chanthabuly District, 01000 Vientiane, Laos",
					"Ssen Mekong;2;https://www.booking.com/hotel/la/muang-lao-mekong-river-side-villa.en-gb.html;Riverfront, Mekong River Road, 06000 Luang Prabang, Laos",
					"Nammavong Guesthouse;2;https://www.booking.com/hotel/la/nammavong-guesthouse.en-gb.html;Ban phone houang Sisalearmsak Road , 06000 Luang Prabang, Laos",
					"Niny Backpacker hotel;1;https://www.booking.com/hotel/la/niny-backpacker.en-gb.html;Next to Wat Mixay, Norkeokhunmane Road., 01000 Vientiane, Laos",
					"Niraxay Apartment;2;https://www.booking.com/hotel/la/niraxay-apartment.en-gb.html;Samsenthai Road Ban Sihom , 01000 Vientiane, Laos",
					"Pakse Mekong Hotel;2;https://www.booking.com/hotel/la/pakse-mekong.en-gb.html;No 062 Khemkong Road, Pakse District, Champasak, Laos, 01000 Pakse, Laos",
					"Phakchai Hotel;2;https://www.booking.com/hotel/la/phakchai.en-gb.html;137 Ban Wattay Mueng Sikothabong Vientiane Laos, 01000 Vientiane, Laos",
					"Phetmeuangsam Hotel;2;https://www.booking.com/hotel/la/phetmisay.en-gb.html;Ban Phanhxai, Xumnuea, Xam Nua, 01000 Xam Nua, Laos" };

			Random r = new Random(0);
			for (String hotel : hotelData) {
				String[] split = hotel.split(";");
				Hotel h = new Hotel();
				h.setName(split[0]);
				h.setRating(split[1]);
				h.setUrl(split[2]);
				h.setAddress(split[3]);
				h.setCategory(HotelCategory.values()[r.nextInt(HotelCategory.values().length)]);
				int daysOld = 0 - r.nextInt(365 * 30);
				h.setOperatesFrom((LocalDate.now().plusDays(daysOld)));
				save(h);
			}
		}
	}
	
	private static class HotelByIdComparator implements Comparator<Hotel> {
		
		@Override
		public int compare(Hotel h1, Hotel h2) {
			return (int) (h2.getId() - h1.getId());
		}

	}

}
