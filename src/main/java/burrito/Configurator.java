package burrito;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import burrito.links.Linkable;
import burrito.sitelet.Sitelet;

import siena.Model;

public abstract class Configurator implements ServletContextListener {

	public static List<Class<? extends Model>> crudables = new ArrayList<Class<? extends Model>>();
	public static List<Class<? extends Sitelet>> sitelets = new ArrayList<Class<? extends Sitelet>>();
	public static List<Class<? extends burrito.links.Linkable>> linkables = new ArrayList<Class<? extends Linkable>>();
	private static String SITE_IDENTIFIER;
	private static String FEEDS_SERVER;

	/**
	 * Make an entity appear in the automated CRUD admin area.
	 * 
	 * @param m
	 */
	public void addCrudable(Class<? extends Model> m) {
		crudables.add(m);
	}

	public void addLinkable(Class<? extends Linkable> link) {
		linkables.add(link);
	}

	public void addSitelet(Class<? extends Sitelet> sitelet) {
		sitelets.add(sitelet);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// do nothing
	}

	/**
	 * Initializes the configurator
	 */
	protected abstract void init();

	protected abstract String configureSiteIdentifier();
	protected String configureFeedsServer() {
		return "";
	}
	
	public static String getSiteIdentifier() {
		return SITE_IDENTIFIER;
	}
	
	public static String getFeedsServer() {
		return FEEDS_SERVER;
	}
	
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		init();
		FEEDS_SERVER = configureFeedsServer();
		SITE_IDENTIFIER = configureSiteIdentifier();
	}

}
