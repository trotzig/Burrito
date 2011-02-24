package burrito.protector;

import javax.servlet.http.HttpServletRequest;

import burrito.Configurator;

import taco.Protector;

/**
 * Protector for background tasks (cron, task queue)
 * 
 * @author henper
 * 
 */
public class BackgroundTaskProtector implements Protector {

	@Override
	public boolean allow(HttpServletRequest request) {
		boolean admin = Configurator.getAdminProtector().allow(request);
		if (admin) {
			return true;
		}
		String queue = request.getHeader("X-AppEngine-QueueName");
		if (queue != null) {
			return true;
		}
		String cron = request.getHeader("X-AppEngine-Cron");
		if (cron != null) {
			return true;
		}
		// neither admin, cron or task queue
		return false;
	}

}
