package burrito;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.PersistenceManager;
import siena.PersistenceManagerFactory;
import siena.core.PersistenceManagerLifeCycleWrapper;
import siena.core.lifecycle.PostDelete;
import siena.core.lifecycle.PostInsert;
import siena.core.lifecycle.PostSave;
import siena.core.lifecycle.PostUpdate;
import siena.gae.GaePersistenceManager;
import burrito.services.SearchManager;

/**
 * Useful base class for all entities. Provides support for automatically
 * updating the search index on save.
 * 
 * @author henper
 * 
 */
public abstract class BurritoModel extends Model {
	
	private static boolean lifecycleEnabled = false;

	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	
	/**
	 * Creates a new BurritoModel.
	 * 
	 */
	public BurritoModel() {
		if (!lifecycleEnabled) {
			//To add support for @Pre... and @Post...-annotations, we need to programmatically tell Siena about this class. 
			synchronized (BurritoModel.class) {
				PersistenceManager pm = new GaePersistenceManager();
				pm = new PersistenceManagerLifeCycleWrapper(pm);
				pm.init(null);
				PersistenceManagerFactory.install(pm, this.getClass());
				lifecycleEnabled = true;
			}
		}
	}

	@PostInsert
	@PostUpdate
	@PostSave
	public final void reindex() {
		if (updateSearchIndexOnSave()) {
			SearchManager.get().insertOrUpdateSearchEntry(this, id);
		}
	}

	@PostDelete
	public final void removeFromIndex() {
		if (updateSearchIndexOnSave()) {
			SearchManager.get().deleteSearchEntry(this.getClass(), id);
		}
	}

	/**
	 * Decides whether search index is updated on save/delete or not. 
	 * Default implementation will always return <code>true</code>. Override to gain control.
	 * 
	 * @return
	 */
	protected boolean updateSearchIndexOnSave() {
		return true;
	}

	/**
	 * Gets the autoincrement database id
	 *  
	 * @return
	 */
	public Long getId() {
		return id;
	}

}
