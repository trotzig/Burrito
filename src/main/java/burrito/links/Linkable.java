package burrito.links;

/**
 * Interface used on entities that has a unique url to a section of the page
 * where it is hosted
 * 
 * @author henper
 * 
 */
public interface Linkable {

	/**
	 * Gets the unique URL to where this entity is located on the site.
	 * 
	 * @return
	 */
	String getUrl();

}
