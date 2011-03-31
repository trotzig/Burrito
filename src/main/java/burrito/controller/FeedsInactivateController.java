package burrito.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import taco.Controller;
import burrito.services.FeedsSubscription;

/**
 * Inactivates all feeds that are older than five minutes
 * 
 * @author henper
 * 
 */
public class FeedsInactivateController implements Controller<Void> {

	@Override
	public Void execute() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		Date fiveMinutesAgo = cal.getTime();
		List<FeedsSubscription> actives = FeedsSubscription.all()
				.filter("active", true).fetch();
		for (FeedsSubscription fs : actives) {
			if (fs.getTimestamp().before(fiveMinutesAgo)) {
				fs.setActive(false);
				fs.update();
			}
		}
		return null;
	}
}
