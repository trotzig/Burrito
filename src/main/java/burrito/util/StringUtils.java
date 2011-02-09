package burrito.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;


public class StringUtils {

	public static final String uniqueNameRegexp = "^[0-9a-z-]+$";
	public static final String uniqueNameDescription = "Endast små bokstäver (a-z), siffror och bindestreck är tillåtna.";
	private static Pattern scriptPattern = Pattern.compile("<script[^>]*>.*</script>", Pattern.DOTALL);
	
	public static String makeSEOFriendly(String s) {
		s = s.toLowerCase();
		s = s.replaceAll("\\s", "-");
		s = s.replaceAll("[åä]", "a");
		s = s.replaceAll("[ö]", "o");
		s = s.replaceAll("[^0-9a-zåäö\\-]", "");
		s = s.replaceAll("-+", "-");
		return s;
	}

	public static String stripHTML(String string) {
		string = stripScripts(string);
		string = stripComments(string);
		string = string.replaceAll("<.*?>", "");
		string = string.replaceAll("&nbsp;", " ");
		return string.trim();
	}

	private static String stripScripts(String string) {
		Matcher m = scriptPattern.matcher(string);
		return m.replaceAll("");
		
	}

	private static String stripComments(String string) {
		int start = string.indexOf("<!--");

		if (start >= 0) {
			String temp = string.substring(0, start);

			int end = string.indexOf("-->", start + 4);
			if (end >= 0) temp += string.substring(end + 3);

			string = stripComments(temp);
		}

		return string;
	}

	public static String stripAndDecodeHTML(String string) {
		if (string != null) string = StringEscapeUtils.unescapeHtml(stripHTML(string));
		return string;
	}

	public static String formatHeaderWithoutLineBreaks(String string) {
		if (string != null) string = string.replace('|', ' ');
		return string;
	}

	public static String formatHeaderAsHTMLWithLineBreaks(String string) {
		if (string != null) string = StringEscapeUtils.escapeHtml(string).replaceAll("\\|", "<br />");
		return string;
	}

	public static String cut(String string, int maxLength) {
		return cut(string, maxLength, true);
	}
	
	/*
	 * Returnerar en förkortad variant av input-strängen som bryter efter ett ord. Lägger på tre punkter på slutet.
	 */
	public static String cut(String original, int maxLength, boolean addDots) {
		if (original == null) {
			return null;
		}
		String trimmed = original.trim();
		if (trimmed.length() < maxLength) {
			// no action required
			return trimmed;
		}
		if (addDots && maxLength < 3) {
			throw new IllegalStateException(
					"maxLength must be larger than 3 if addDots is true");
		}
		int max = (addDots) ? maxLength - 3 : maxLength;

		if (max < 0) {
			throw new IllegalStateException(
					"Max length is not large enough to display any text");
		}

		String cutEnd = null;
		try {
			cutEnd = trimmed.substring(0, max);
		} catch (IndexOutOfBoundsException e) {
			cutEnd = trimmed;
		}
		int lastSpaceIndex = cutEnd.lastIndexOf(' ');
		if (lastSpaceIndex == -1 || lastSpaceIndex == 0) {
			// no space in text, simply cut the text anyway
			if (addDots) {
				return cutEnd + "...";
			}
			return cutEnd;
		}
		String nicelyCut = cutEnd.substring(0, lastSpaceIndex);
		if (addDots) {
			return nicelyCut + "...";
		}
		return nicelyCut;
	}

	public static String escapeJavascript(String s) {
		return StringEscapeUtils.escapeJavaScript(s);
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
	
	
}
