package burrito.services;

import java.io.Serializable;
import java.util.List;

import burrito.annotations.Cache;
import burrito.sitelet.Sitelet;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;

public class SiteletProperties extends Model implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(Generator.AUTO_INCREMENT)
	public Long id;

	public Integer order;

	public String containerId;

	public String entityTypeClassName;

	public Long entityId;

	private transient Boolean cacheEnabled;

	private transient Integer cacheExpirationInSeconds;

	public static Query<SiteletProperties> all() {
		return Model.all(SiteletProperties.class);
	}

	public static SiteletProperties get(Long id) {
		return all().filter("id", id).get();
	}

	public static List<SiteletProperties> getByContainerId(String containerId) {
		return all().filter("containerId", containerId).order("order").fetch();
	}

	/**
	 * Gets the key to use when caching content within this sitelet.
	 * 
	 * @return
	 */
	public String getCacheKey() {
		return containerId + id;
	}

	public static SiteletProperties getByEntityId(Long entityId) {
		return all().filter("entityId", entityId).get();
	}

	/**
	 * Gets the cache expiration time for this sitelet. This time is taken from
	 * the embedded sitelet entity and its {@link Cache} annotation.
	 * 
	 * @return
	 */
	public int getCacheExpirationInSeconds() {
		ensureCacheResolved();
		return cacheExpirationInSeconds.intValue();
	}

	/**
	 * Decides whether this sitelet can be cached or not.
	 * 
	 * @return
	 */
	public boolean isCacheEnabled() {
		ensureCacheResolved();
		return cacheEnabled.booleanValue();
	}

	private void ensureCacheResolved() {
		if (cacheEnabled != null) {
			// already resolved
			return;
		}
		try {
			Class<?> clazz = Class.forName(entityTypeClassName);
			burrito.annotations.Cache cache = clazz.getAnnotation(burrito.annotations.Cache.class);
			if (cache == null) {
				disableCache();
				return;
			}
			cacheExpirationInSeconds = cache.expirationInSeconds();
			cacheEnabled = cache.enabled();
		} catch (ClassNotFoundException e) {
			disableCache();
		}
	}

	private void disableCache() {
		cacheEnabled = false;
		cacheExpirationInSeconds = 0;
	}
	
	@SuppressWarnings("unchecked")
	public Sitelet getAssociatedSitelet() {
		try {
			return (Sitelet) Model.all(
					(Class<? extends Model>) Class.forName(this.entityTypeClassName)).filter(
					"id", entityId).get();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unknown class: " + entityTypeClassName, e);
		}
	}

	public Long getId() {
		return id;
	}
	
	

}
