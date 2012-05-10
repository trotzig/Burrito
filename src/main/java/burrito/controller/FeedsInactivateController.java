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
