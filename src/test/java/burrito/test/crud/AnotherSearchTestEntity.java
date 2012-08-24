package burrito.test.crud;

import java.util.Date;

import burrito.BurritoModel;
import burrito.annotations.Displayable;
import burrito.annotations.SearchableField;
/**
 * This class is a copy of the {@link SearchTestEntity} class, but with different datatypes. 
 * @author henper
 *
 */
public class AnotherSearchTestEntity extends BurritoModel {
	
	
	@SearchableField
	private Long name;

	@Displayable
	private String date;
	
	@Displayable
	private Date displayableField;

	@Displayable
	private String number;
	
	private String lastModified;

	public Long getName() {
		return name;
	}

	public void setName(Long name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Date getDisplayableField() {
		return displayableField;
	}

	public void setDisplayableField(Date displayableField) {
		this.displayableField = displayableField;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	
	
	
}
