package burrito;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.search.DeleteException;
import com.google.appengine.api.search.PutException;

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
import burrito.services.SearchManagerFactory;
import burrito.util.Logger;

/**
 * Useful base class for all entities. Provides support for automatically
 * updating the search index on save.
 * 
 * @author henper
 * 
 */
public abstract class BurritoModel extends Model {
	
	private final static Set<Class<?>> LIFE_CYCLE_ENABLED_CLASSES = Collections.synchronizedSet(new HashSet<Class<?>>());

	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	
	/**
	 * Creates a new BurritoModel.
	 * 
	 */
	public BurritoModel() {
		if (!LIFE_CYCLE_ENABLED_CLASSES.contains(this.getClass())) {
			//To add support for @Pre... and @Post...-annotations, we need to programmatically tell Siena about this class. 
			synchronized (LIFE_CYCLE_ENABLED_CLASSES) {
				PersistenceManager pm = new GaePersistenceManager();
				pm = new PersistenceManagerLifeCycleWrapper(pm);
				pm.init(null);
				PersistenceManagerFactory.install(pm, this.getClass());
				LIFE_CYCLE_ENABLED_CLASSES.add(this.getClass());
			}
		}
	}

	@PostInsert
	@PostUpdate
	@PostSave
	public final void reindex() {
		if (updateSearchIndexOnSave()) {
			try {
				SearchManagerFactory.getSearchManager().insertOrUpdateSearchEntry(this, id);
			} catch (PutException e) {
				Logger.error("Could not update search entry for " + toString(), e);
			}
		}
	}

	@PostDelete
	public final void removeFromIndex() {
		if (updateSearchIndexOnSave()) {
			try {
				SearchManagerFactory.getSearchManager().deleteSearchEntry(this.getClass(), id);
			} catch (DeleteException e) {
				Logger.error("Could not update search entry for " + toString(), e);
			}
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
