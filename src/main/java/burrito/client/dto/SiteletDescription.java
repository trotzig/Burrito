package burrito.client.dto;

import java.io.Serializable;

public class SiteletDescription implements Serializable {

	private static final long serialVersionUID = 1L;

	private String entityName;
	private Long entityId;
	private String description;

	public SiteletDescription(String entityName, Long entityId, String description) {
		this.entityName = entityName;
		this.entityId = entityId;
		this.description = description;
	}

	public SiteletDescription() {
		// default constructor
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
