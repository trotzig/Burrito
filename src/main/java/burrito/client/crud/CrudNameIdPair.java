package burrito.client.crud;

import java.io.Serializable;

/**
 * Describes an entity in a short form, to be used as info when rendering list
 * boxes. This class is used with RPC to transfer objects between client (GWT)
 * and server.
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class CrudNameIdPair implements Serializable {

	private Long id;
	private String displayName;

	public CrudNameIdPair(Long id, String displayName) {
		this.displayName = displayName;
		this.id = id;
	}

	public CrudNameIdPair() {
		// default constructor
	}

	/**
	 * Gets the database id
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the database id
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the display name
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Stes the display name
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public boolean equals(Object obj) {
		return id.longValue() == ((CrudNameIdPair) obj).getId().longValue();
	}

}
