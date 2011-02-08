package burrito.client.crud.generic;

import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.Table;

/**
 * Describes a collection of entities of a certain type. Instances of this class
 * is used as input to {@link Table}s
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class CrudEntityList extends ItemCollection<CrudEntityDescription> {

	private String entityName;

	/**
	 * Sets the entity name. This is usually the classname of a database entity,
	 * e.g. "models.Program"
	 * 
	 * @param entityName
	 */
	public void setEntityName(String entityName) {
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

}
