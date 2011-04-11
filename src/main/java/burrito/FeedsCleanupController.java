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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import taco.Controller;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * Controller which can be configured as a cron job to handle cleanup of old and
 * orphaned {@FeedsSubscription} and {@FeedsSubscriptionMessage} entities.
 * 
 * @author Joakim Söderström
 *
 */
public class FeedsCleanupController implements Controller<Map<String, String>>{

	private static final int MAX_FEEDS_SUBSCRIPTION_COUNT = 1000;
	private static final int MAX_FEEDS_SUBSCRIPTION_MESSAGE_COUNT = 1000;

	private DatastoreService datastore;

	private Integer nrOfDays;
	

	public void setNrOfDays(Integer nrOfDays) {
		this.nrOfDays = nrOfDays;
	}
	
	@Override
	public Map<String, String> execute() {
		datastore = DatastoreServiceFactory.getDatastoreService();
		Map<String, String> result = new HashMap<String, String>();
		if(nrOfDays > 0) {
			int nrOfDeletedEntities = 0;
			Iterator<Entity> entities = fetchOrphanedFeedsSubscriptions();
			while (entities.hasNext()) {
				datastore.delete(entities.next().getKey());
				nrOfDeletedEntities++;
			}
			entities = fetchOrphanedFeedsSubscriptionMessages();
			while(entities.hasNext()) {
				datastore.delete(entities.next().getKey());
				nrOfDeletedEntities++;
			}
			result.put("success", nrOfDeletedEntities + " old feeds removed.");
			
		} else {
			result.put("error", "nrOfDays must be positive");
		}
		return result;
	}
	
	
	private Iterator<Entity> fetchOrphanedFeedsSubscriptions() {
		return fetchEntitiesByTimestamp("FeedsSubscription", MAX_FEEDS_SUBSCRIPTION_COUNT);
	}
	
	private Iterator<Entity> fetchOrphanedFeedsSubscriptionMessages() {
		return fetchEntitiesByTimestamp("FeedsSubscriptionMessage", MAX_FEEDS_SUBSCRIPTION_MESSAGE_COUNT);
	}
	
	private Iterator<Entity> fetchEntitiesByTimestamp(String entityName, int limit) {
		Query query = new Query(entityName);
		query.addFilter("timestamp", FilterOperator.LESS_THAN_OR_EQUAL, getStopDate());
		query.addSort("timestamp", SortDirection.ASCENDING);
		query.setKeysOnly();
		PreparedQuery pq = datastore.prepare(query);
		return pq.asIterator(FetchOptions.Builder.withLimit(limit));
	}

	private Date getStopDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -nrOfDays);
		return calendar.getTime();
	}
}
