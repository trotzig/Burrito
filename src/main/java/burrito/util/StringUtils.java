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

package burrito.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gdata.util.common.base.CharEscapers;


public class StringUtils {

	public static final char[] randomStringAlphabet = "abcdefghjkmnpqrstuvwxy3456789".toCharArray();
	
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
	
	public static String stripBBCode(String string) {
		return string.replaceAll("\\[.*?\\]", "");
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
	
	/**
	 * Tar en sträng och returnerar en där alla html-osäkra tecken har bytts ut
	 * mot html-säkra varianter. Ex: &lt;b&gt; blir "&amp;lt;b&amp;gt;"
	 * 
	 * @param html
	 * @return
	 */
	public static String escapeHtml(String html) {
		return CharEscapers.htmlEscaper().escape(html);
	}
	
	/**
	 * Genererar en random sträng
	 * @param length
	 * @return
	 */
	public static String generateRandomString(int length) {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			char[] chars = new char[length];

			for (int i = 0; i < length; i++) {
				chars[i] = randomStringAlphabet[random.nextInt(randomStringAlphabet.length)];
			}

			return String.valueOf(chars);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * 
	 * @param str "asdasd=qweqwe"
	 * @param delimiter "="
	 * @return "asdasd"
	 */
	public static String substringBefore(String str, String delimiter) {
		int indexOf = str.indexOf(delimiter);
		return str.substring(0, indexOf);
	}
	
	/**
	 * 
	 * 
	 * @param str "asdasd=qweqwe"
	 * @param delimiter "="
	 * @return "qweqwe"
	 */
	public static String substringAfter(String str, String delimiter) {
		int indexOf = str.indexOf(delimiter);
		return str.substring(indexOf + delimiter.length(), str.length());
	}
	
	
}
