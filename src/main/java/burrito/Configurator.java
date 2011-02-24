package burrito;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import siena.Model;
import taco.Protector;
import burrito.links.Linkable;
import burrito.sitelet.Sitelet;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public abstract class Configurator implements ServletContextListener {

	public static List<Class<? extends Model>> crudables = new ArrayList<Class<? extends Model>>();
	public static List<Class<? extends Sitelet>> sitelets = new ArrayList<Class<? extends Sitelet>>();
	public static List<Class<? extends burrito.links.Linkable>> linkables = new ArrayList<Class<? extends Linkable>>();
	private static String SITE_IDENTIFIER;
	private static BroadcastSettings BROADCAST_SETTINGS;
	private static Protector ADMIN_PROTECTOR;

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

	/**
	 * Return a unique identifier for your site. This could for example be the
	 * id for your GAE app. It is important that this identifier is unique among
	 * all sites connecting to the same feed server. Otherwise you can end up
	 * with messages being broadcasted to the wrong site.
	 * 
	 * @return
	 */
	protected abstract String configureSiteIdentifier();

	/**
	 * Return a "password" string that will be used when broadcasting messages
	 * via the feeds server. All broadcasts will contain this secret and the
	 * feeds server will only let through those that provide a correct secret.
	 * 
	 * @return
	 */
	protected abstract BroadcastSettings configureBroadcastSettings();

	/**
	 * Configures the admin protector. This protector is used to decide whether
	 * the current request is being made from an admin or not.
	 * 
	 * @return
	 */
	protected Protector configureAdminProtector() {
		return new Protector() {

			@Override
			public boolean allow(HttpServletRequest request) {
				UserService service = UserServiceFactory.getUserService();
				if (!service.isUserLoggedIn()) {
					return false;
				}
				return service.isUserAdmin();
			}
		};
	}

	/**
	 * Gets the site identifier configured for this Burrito instance.
	 * 
	 * @return
	 */
	public static String getSiteIdentifier() {
		return SITE_IDENTIFIER;
	}

	/**
	 * Gets the broadcast settings
	 * 
	 * @return
	 */
	public static BroadcastSettings getBroadcastSettings() {
		return BROADCAST_SETTINGS;
	}

	/**
	 * Gets an admin protector
	 * 
	 * @return
	 */
	public static Protector getAdminProtector() {
		return ADMIN_PROTECTOR;
	}

	@Override
	public void contextInitialized(ServletContextEvent ctx) {
		SITE_IDENTIFIER = configureSiteIdentifier();
		BROADCAST_SETTINGS = configureBroadcastSettings();
		ADMIN_PROTECTOR = configureAdminProtector();
		init();
	}

}
