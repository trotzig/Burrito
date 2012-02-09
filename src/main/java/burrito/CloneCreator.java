package burrito;

import siena.Model;

public interface CloneCreator<T extends Model> {

	/**
	 * Creates a clone of the object. Use in your @Cloneable entities to get
	 * more control over the clones that you create.
	 * 
	 * @return A clone of the entity/object at hand
	 */
	T createClone();

}
