package ee.mart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.mart.dao.GuestEntryDao;
import ee.mart.model.GuestEntry;

@Service
public class GuestbookService {
	
	@Autowired
	GuestEntryDao dao;
	
	public void save(GuestEntry entry) {
		dao.save(entry);
	}
	
	public void delete(GuestEntry entry) {
		dao.delete(entry);
	}
	
	public List<GuestEntry> findAllEntries() {
		List<GuestEntry> entries = (List<GuestEntry>) dao.findAll();
		return entries;
	}
	
	public List<GuestEntry> filterEntries(String firstName, String lastName) {
		if(firstName.isEmpty() && lastName.isEmpty()) {
			return (List<GuestEntry>) dao.findAll();
		} else if(firstName.isEmpty() && !lastName.isEmpty()) {
			return dao.findByLastNameContainingIgnoreCase(lastName);
		} else if(!firstName.isEmpty() && lastName.isEmpty()) {
			return dao.findByFirstNameContainingIgnoreCase(firstName);
		} else {
			return dao.findByFirstNameOrLastNameContainingIgnoreCase(firstName, lastName);
		}
	}

}
