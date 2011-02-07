package burrito.util;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class Cache {

	private static MemcacheService memcacheService = MemcacheServiceFactory
			.getMemcacheService();

	public static void put(String key, Object value) {
		memcacheService.put(key, value);
	}

	public static void put(String key, Object value, int expirationInSeconds) {
		memcacheService.put(key, value, Expiration
				.byDeltaSeconds(expirationInSeconds));
	}
	
	public static Object get(String key) {
		return memcacheService.get(key);
	}

	public static void delete(String key) {
		memcacheService.delete(key);
	}

}
