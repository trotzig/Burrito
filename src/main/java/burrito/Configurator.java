/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package burrito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;

import siena.Model;
import taco.Protector;
import burrito.links.Linkable;
import burrito.sitelet.Sitelet;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public abstract class Configurator implements ServletContextListener {

	public static List<Class<? extends Model>> crudables = Collections.synchronizedList(new ArrayList<Class<? extends Model>>());
	public static List<Class<? extends Sitelet>> sitelets = Collections.synchronizedList(new ArrayList<Class<? extends Sitelet>>());
	public static List<Class<? extends burrito.links.Linkable>> linkables = Collections.synchronizedList(new ArrayList<Class<? extends Linkable>>());
	public static boolean MAY_RETIRE_SITELETS = false;
	private static final String DEV_MODE_CONFIGURATION_SUFFIX = "-DEV-" + RandomStringUtils.randomAlphabetic(4);
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
	 * Defines whether sitelets should go into retirement mode if they haven't
	 * been displayed for a while (24 h). In retirement mode, sitelets need to be 
	 * displayed in a page for it to be scheduled for refresh.   
	 * 
	 * default is false 
	 */
	protected boolean mayRetireSitelets() {
		return false;
	}
	
	

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
		if(isDevMode(ctx.getServletContext())) {
			SITE_IDENTIFIER = configureSiteIdentifier() + DEV_MODE_CONFIGURATION_SUFFIX;
		} else {
			SITE_IDENTIFIER = configureSiteIdentifier();
		}
		BROADCAST_SETTINGS = configureBroadcastSettings();
		ADMIN_PROTECTOR = configureAdminProtector();
		MAY_RETIRE_SITELETS = mayRetireSitelets();
		
		init();
	}
	
	private boolean isDevMode(ServletContext context) {
		String serverInfo = context.getServerInfo();
		/*
		 * ServerInfo will look something like "Google App Engine Development/x.x.x" when running in
		 * the development server. In production mode it will look like "Google App Engine/x.x.x"
		 */
		return serverInfo.contains("Development");
	}

}
