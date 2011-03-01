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
