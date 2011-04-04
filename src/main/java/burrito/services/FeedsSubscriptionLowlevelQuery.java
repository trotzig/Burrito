package burrito.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * 
 * A low-level query used to fetch FeedsSubscriptions. The reason we use this
 * class instead of a simple Siena query is that we need chunked results. 
 * 
 * This query is divided into chunks of 200 entities. Each chunk is fetched as 
 * a list, to avoid hanging on to a datastore connection while iterating. 
 * 
 * @author Mikael Claesson, Joakim Söderström, Henric Persson
 * 
 */
public class FeedsSubscriptionLowlevelQuery implements Iterable<FeedsSubscription>, Iterator<FeedsSubscription> {

	private final DatastoreService datastore;
	private final Query query;
	private Cursor nextCursor;
	private Iterator<Entity> currentIterator;

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
		FetchOptions options = FetchOptions.Builder.withLimit(200);
		if (nextCursor != null) {
			options.startCursor(nextCursor);
		}
		//To avoid datastore timeouts while iterating through subscriptions, 
		//We make sure that we fetch entities in chunks, where each chunk is
		//a standalone query. This way we can spend as much time as we want to 
		//in each iteration. 
		QueryResultList<Entity> currentBatch = datastore.prepare(query).asQueryResultList(options);
		if (currentBatch != null) {
			nextCursor = currentBatch.getCursor();
		}
		currentIterator = currentBatch.iterator();
	}

	@Override
	public Iterator<FeedsSubscription> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if (currentIterator == null || !currentIterator.hasNext()) {
			fetchResult();
			onNewBatch(currentIterator);
		}
		return currentIterator.hasNext();
	}

	/**
	 * Method called between every chunked query made. Default implementation
	 * does nothing.
	 * 
	 * @param currentIterator
	 */
	protected void onNewBatch(Iterator<Entity> currentIterator) {
		//Default impl: do nothing
	}

	@Override
	@SuppressWarnings("unchecked")
	public FeedsSubscription next() {
		if (currentIterator == null) fetchResult();

		Entity entity = currentIterator.next();
		if (entity == null) return null;

		FeedsSubscription subscription = new FeedsSubscription();
		subscription.setId((Long) entity.getKey().getId());
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
