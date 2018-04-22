package com.gp.vaadin.hotel_demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gp.vaadin.hotel_demo.model.IdentifiedObject;
import com.gp.vaadin.hotel_demo.model.filter.Filter;

public abstract class AbstractTestDataService<DomainObject extends IdentifiedObject, DomainObjectFilter extends Filter> {
	
	private static final Logger LOGGER = Logger.getLogger(AbstractTestDataService.class.getName());

	private final ConcurrentMap<Long, DomainObject> storage = new ConcurrentHashMap<>();
	private AtomicLong nextId = new AtomicLong(1);

	public List<DomainObject> findAll() {
		return findAll(null);
	}

	public DomainObject getById(Long id) {
		return id == null ? null : storage.get(id);
	}
	
	public List<DomainObject> findAll(DomainObjectFilter filter) {
		List<DomainObject> filteredObjects = new ArrayList<>();
		for (DomainObject domainObject : storage.values()) {
			if (isPassesFilter(domainObject, filter)) {
				filteredObjects.add(copy(domainObject));
			}
		}
		Collections.sort(filteredObjects, new DefaultIdentifiedObjectComparator());
		return filteredObjects;
	}
	
	protected boolean isContainsSubstring(String text, String pattern) {
		return pattern == null || pattern.isEmpty() || text.toLowerCase().contains(pattern.toLowerCase());
	}
	
	public List<DomainObject> findAll(DomainObjectFilter filter, int start, int maxresults) {
		List<DomainObject> filteredObjects = new ArrayList<>();
		for (DomainObject domainObject : storage.values()) {
			if (isPassesFilter(domainObject, filter)) {
				filteredObjects.add(copy(domainObject));
			}
		}
		Collections.sort(filteredObjects, new DefaultIdentifiedObjectComparator());
		int end = start + maxresults;
		if (end > filteredObjects.size()) {
			end = filteredObjects.size();
		}
		return filteredObjects.subList(start, end);
	}

	public long count() {
		return storage.size();
	}

	public void delete(Collection<DomainObject> domainObjects) {
		for (DomainObject domainObject : domainObjects) {
			delete(domainObject);
		}
	}
	
	public void delete(DomainObject domainObject) {
		storage.remove(domainObject.getId());
	}

	public void save(DomainObject entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "DomainObject is null.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId.getAndIncrement());
		}
		try {
			entry = copy(entry);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		storage.put(entry.getId(), entry);
	}

	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] testData = getTestData();

			Random r = new Random(0);
			for (String objectString : testData) {
				String[] split = objectString.split(";");
				DomainObject domainObject = createTestObject(split, r);
				save(domainObject);
			}
		}
	}
	
	protected abstract DomainObject copy(DomainObject domainObject);
	
	protected abstract boolean isPassesFilter(DomainObject domainObject, DomainObjectFilter filter);
	
	protected abstract DomainObject createTestObject(String[] split, Random r);

	protected abstract String[] getTestData();
	
	private static class DefaultIdentifiedObjectComparator implements Comparator<IdentifiedObject> {
		
		@Override
		public int compare(IdentifiedObject o1, IdentifiedObject o2) {
			return (int) (o2.getId() - o1.getId());
		}

	}
	
}
