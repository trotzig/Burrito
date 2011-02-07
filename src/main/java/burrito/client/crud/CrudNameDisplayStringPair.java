package burrito.client.crud;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CrudNameDisplayStringPair implements Serializable {

	private String value;
	private String displayString;

	public CrudNameDisplayStringPair() {
		// Default constructor
	}

	public CrudNameDisplayStringPair(String value, String displayString) {
		this.value = value;
		this.displayString = displayString;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

}
