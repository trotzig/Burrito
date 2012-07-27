package burrito.client.util;


/**
 * A string cutter can cut strings at a given max length. It is smart enough not
 * to cut in the middle of words.
 * 
 * 
 * 
 * @author henper
 * 
 */
public class StringCutter {

	private int maxLength;

	/**
	 * Creates a {@link StringCutter} that knows how to cut strings at a given
	 * maxLength
	 * 
	 * @param maxLength
	 */
	public StringCutter(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Cuts a string at the max length, starting from position 0 in the string.
	 * If addDots is true, then "..." is appended to the end of the returned
	 * string
	 * 
	 * @param original
	 * @return
	 */
	public String cut(String original, boolean addDots) {
		return cut(original, addDots, 0);
	}

	/**
	 * Cuts a string at maxLength size and makes sure that the i position is
	 * contained in the returned string
	 * 
	 * @param original
	 * @param addDots
	 * @param i
	 */
	public String cut(String original, boolean addDots, int i) {
		int startPos = 0;
		if (i != 0) {
			startPos = i - (maxLength / 2);
			if (startPos < 0) {
				startPos = 0;
			}
			if (addDots && startPos != 0) {
				startPos += 3;
			}
		}
		if (original == null) {
			return null;
		}
		if (original.length() < maxLength) {
			// no action required
			return original;
		}
		if (addDots && maxLength < 3) {
			throw new IllegalStateException(
					"maxLength must be larger than 3 if addDots is true");
		}
		int max = (addDots) ? maxLength - 3 : maxLength;
		if (startPos != 0 && addDots) {
			max -= 3;
		}

		if (max < 0) {
			throw new IllegalStateException(
					"Max length is not large enough to display any text");
		}

		String cutStart = original.substring(startPos);
		if (startPos != 0) {
			int firstSpaceIndex = cutStart.indexOf(' ');
			if (firstSpaceIndex != -1 && firstSpaceIndex < 4) {
				cutStart = cutStart.substring(firstSpaceIndex + 1);
			}
		}
		String cutEnd = null;
		try {
			cutEnd = cutStart.substring(0, max);
		} catch (IndexOutOfBoundsException e) {
			cutEnd = cutStart;
		}
		String nicelyCut;
		boolean addEndDots = true;
		if (original.endsWith(cutEnd)) {
			//Ends with a word
			nicelyCut = cutEnd;
			addEndDots = false;
		} else {
			int lastSpaceIndex = cutEnd.lastIndexOf(' ');
			if (lastSpaceIndex == -1 || lastSpaceIndex == 0) {
				// no space in text, simply cut the text anyway
				if (addDots) {
					return cutEnd + "...";
				}
				return cutEnd;
			}
			nicelyCut = cutEnd.substring(0, lastSpaceIndex);
		}
		if (addDots) {
			if (startPos != 0) {
				nicelyCut = "..." + nicelyCut;
			}
			if (addEndDots) {
				nicelyCut = nicelyCut + "...";
			}
		}
		return nicelyCut;
	}

	/**
	 * Cuts a string at maxLength size and adds "..." to the end of the string
	 * 
	 * @param original
	 * @return
	 */
	public String cut(String original) {
		return cut(original, true);
	}

	/**
	 * Sets the maxlength of the returned strings
	 * 
	 * @param maxLength
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

}
