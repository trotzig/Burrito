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
