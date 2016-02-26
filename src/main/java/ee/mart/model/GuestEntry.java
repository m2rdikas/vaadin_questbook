package ee.mart.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class GuestEntry {
	
	@Id
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequenceName", value = "sequence"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "sequence", strategy=GenerationType.SEQUENCE)
	private Long id;
	private String firstName;
	private String lastName;
	private String text;
	
	public GuestEntry() {
		
	}
	
	public GuestEntry(String firstName, String lastName, String text) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.text = text;
	}
	
}
