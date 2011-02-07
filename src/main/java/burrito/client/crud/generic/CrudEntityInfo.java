package burrito.client.crud.generic;

import java.io.Serializable;

/**
 * Simple description of a crud enabled entity
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class CrudEntityInfo implements Serializable {

	private String entityName;

	public CrudEntityInfo() {
		// default constructor
	}

	public CrudEntityInfo(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Gets the entity name. This is usually the classname of a database entity,
	 * e.g. "models.Program"
	 * 
	 * @return
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Sets the entity name. This is usually the classname of a database entity,
	 * e.g. "models.Program"
	 * 
	 * @param entityName
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

}
