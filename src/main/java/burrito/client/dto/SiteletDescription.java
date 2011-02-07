package burrito.client.dto;

import java.io.Serializable;

public class SiteletDescription implements Serializable {

	private static final long serialVersionUID = 1L;

	private String entityName;
	private Long entityId;

	public SiteletDescription(String entityName, Long entityId) {
		this.entityName = entityName;
		this.entityId = entityId;
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

}
