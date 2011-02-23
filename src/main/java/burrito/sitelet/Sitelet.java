package burrito.sitelet;

/**
 * Common interface used for all sitelets
 * 
 * @author henper
 * 
 */
public interface Sitelet {

	/**
	 * Gets a date when this sitelet should be updated next time. Return
	 * <code>null</code> if the sitelet does not need to be auto refreshed.
	 * (Useful in cases when the only action that can change the ouput is from
	 * changing the sitelets settings)
	 * 
	 * @return
	 */
	public AutoRefresh getNextAutoRefresh();
}
