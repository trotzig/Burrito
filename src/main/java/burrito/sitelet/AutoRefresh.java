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

package burrito.sitelet;

import java.util.Calendar;
import java.util.Date;

/**
 * Description of a sitelets next update
 * 
 * @author henper
 * 
 */
public class AutoRefresh {

	private Date time;

	/**
	 * Sets a date as the next update
	 * 
	 * @param time
	 */
	public AutoRefresh(Date time) {
		this.time = time;
	}

	/**
	 * Describes an update that should be done in x minutes
	 * 
	 * @param minutes
	 */
	public AutoRefresh(int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minutes);
		this.time = cal.getTime();
	}

	/**
	 * Gets the Date when the update should be performed.
	 * 
	 * @return
	 */
	public Date getTime() {
		return time;
	}

}
