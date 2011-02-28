package burrito.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class FeedsSubscriptionLowlevelQuery implements Iterable<FeedsSubscription>, Iterator<FeedsSubscription> {

	private final DatastoreService datastore;
	private final Query query;
	private Iterator<Entity> result;

	public FeedsSubscriptionLowlevelQuery() {
		datastore = DatastoreServiceFactory.getDatastoreService();
		query = new Query("FeedsSubscription");
	}

	public FeedsSubscriptionLowlevelQuery addFilter(String propertyName, FilterOperator operator, Object value) {
		query.addFilter(propertyName, operator, value);
		return this;
	}

	public FeedsSubscriptionLowlevelQuery addSort(String propertyName, SortDirection direction) {
		query.addSort(propertyName, direction);
		return this;
	}

	private void fetchResult() {
		result = datastore.prepare(query).asIterator(FetchOptions.Builder.withChunkSize(100));
	}

	@Override
	public Iterator<FeedsSubscription> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if (result == null) fetchResult();

		return result.hasNext();
	}

	@Override
	@SuppressWarnings("unchecked")
	public FeedsSubscription next() {
		if (result == null) fetchResult();

		Entity entity = result.next();
		if (entity == null) return null;

		FeedsSubscription subscription = new FeedsSubscription();
		subscription.setClientId((String) entity.getProperty("clientId"));
		subscription.setChannelId((String) entity.getProperty("channelId"));
		subscription.setFeedIds((List<String>) entity.getProperty("feedIds"));
		subscription.setCreated((Date) entity.getProperty("created"));
		subscription.setTimestamp((Date) entity.getProperty("timestamp"));

		return subscription;
	}

	@Override
	public void remove() {
		// not implemented
	}
}
