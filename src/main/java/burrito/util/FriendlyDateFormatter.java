package burrito.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Formats dates relatively to the current time. See format() for docs.
 * 
 * @author henper
 * 
 */
public class FriendlyDateFormatter {

	private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm";

	private static final int ONE_SECOND_MS = 1000;
	private static final int ONE_MINUTE_MS = ONE_SECOND_MS * 60;
	private static final int TWO_AND_A_HALF_MINUTES_MS = (int) ((double) ONE_MINUTE_MS * 2.5d);
	public static final TimeZone TIMEZONE_EUROPE_STOCKHOLM = TimeZone.getTimeZone("Europe/Stockholm");
	public static final Locale LOCALE_SWEDISH = new Locale("sv");

	public static String formatDate(Date date) {
		return formatDate(date, null, null);
	}

	
	public static String formatDate(Date date, String pattern, String locale) {
		if (date == null) date = new Date();
		if (pattern == null || pattern.length() == 0) {
			return createDateFormat(DEFAULT_PATTERN).format(date);
		}
		
		Locale l = LOCALE_SWEDISH;
		if (locale != null && !locale.isEmpty()) {
			l = new Locale(locale);
		}
		return createDateFormat(pattern, l).format(date);
	}

	private static DateFormat createDateFormat(String pattern) {
		return createDateFormat(pattern, LOCALE_SWEDISH);
	}

	private static DateFormat createDateFormat(String pattern, Locale locale) {
		SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
		df.setTimeZone(TIMEZONE_EUROPE_STOCKHOLM);
		return df;
	}

	public static void roundToNearestFiveMinute(Date toBeRounded) {
		long time = toBeRounded.getTime();

		// Add 2.5 minutes to time, to avoid always rounding up
		time = time + TWO_AND_A_HALF_MINUTES_MS;
		long diff = time % (1000 * 60 * 5);
		toBeRounded.setTime(time - diff);
	}

	/**
	 * Formats a number of seconds to hours, minutes and seconds.
	 * 
	 * @param seconds
	 * @return
	 */
	public static String formatSeconds(long seconds) {
		StringBuilder sb = new StringBuilder();
		long hours = seconds / (3600);
		if (hours > 0) {
			sb.append(hours + " h");
		}
		long secondsRemaining = seconds % (3600);
		long minutes = secondsRemaining / 60;
		if (minutes > 0) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(minutes + " min");
		}
		long secs = secondsRemaining % 60;

		if (secs > 0 || sb.length() == 0) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(secs + " s");
		}
		return sb.toString();
	}
	
	public static String formatSeconds(int seconds) {
		return formatSeconds((long)seconds);
	}
}
