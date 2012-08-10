package burrito.services;

import burrito.Configurator;
/**
 * Factory class for {@link SearchManager}
 * 
 * @author henper
 *
 */
public class SearchManagerFactory {
	static SearchManager instance;

	
	/**
	 * Gets a searchmanager instance (singleton)
	 * 
	 * @return
	 */
	public static SearchManager getSearchManager() {
		if (instance == null) {
			try {
				instance = Configurator.SEARCH_MANAGER_TYPE.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Failed to create search manager of type " + Configurator.SEARCH_MANAGER_TYPE + 
						". Search manager type is set in burrito.Configurator.SEARCH_MANAGER_TYPE.", e);
			}
			
		}
		return instance;
	}
	
	/**
	 * Destroys the singleton SearchManager instance 
	 */
	public static void destroySearchManager() {
		instance = null;
	}
	

}
