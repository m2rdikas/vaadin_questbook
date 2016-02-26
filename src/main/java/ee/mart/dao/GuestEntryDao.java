package ee.mart.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ee.mart.model.GuestEntry;

@Repository
public interface GuestEntryDao extends CrudRepository<GuestEntry, Long> {
	
	public List<GuestEntry> findByLastNameContainingIgnoreCase(String lastName); 
	
	public List<GuestEntry> findByFirstNameContainingIgnoreCase(String firstName);
	
	public List<GuestEntry> findByFirstNameOrLastNameContainingIgnoreCase(String firstName, String lastName);

}
