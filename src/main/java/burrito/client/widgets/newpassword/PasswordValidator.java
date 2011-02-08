package burrito.client.widgets.newpassword;

/**
 * Validates a password to see if it is good enough.
 * 
 * @author darvod
 * 
 */
public class PasswordValidator {
	private static int passwordMinimumLength = 6;

	/**
	 * Returns a number between 0 and 10 showing the strength of the password
	 * 
	 * @param password
	 * @return
	 */
	public int getStrength(String password) {
		return checkPasswordStrength(password);
	}

	/**
	 * Returns true if the password is valid (At least 6 characters long)
	 * 
	 * @param password
	 * @return
	 */
	public boolean isValid(String password) {
		if (password.length() >= passwordMinimumLength) {
			return true;
		}
		return false;
	}

	/**
	 * Code from http://justwild.us/examples/password/
	 * 
	 * Heavily modified. Returns a value between 0 and 10 instead of printing
	 * out the results and a boolean.
	 * 
	 * @param passwd
	 * @return Max value 10, min value 0 (Will only return 0 if the password
	 *         length == 0.
	 */
	public int checkPasswordStrength(String passwd) {
		// TODO: too simple?
		boolean upper = false, lower = false, numbers = false, special = false;
		int intScore = 0, length = 0;
		if (passwd == null)
			return 0;
		// PASSWORD LENGTH
		length = passwd.length();
		if (length > passwordMinimumLength) {// if password length passes the
			// minimum
			intScore += 1;
		}
		// LETTERS
		if (passwd.matches(".*[a-z]+.*")) {// lower case letters
			lower = true;
			intScore = intScore + 1;
		}
		if (passwd.matches(".*[A-Z]+.*")) {// upper case letters
			upper = true;
			intScore += 1;
		}
		// NUMBERS
		if (passwd.matches(".*[0-9]+.*")) {
			numbers = true;
			intScore += 1;
		}
		// SPECIAL CHAR
		if (passwd.matches(".*[:,!,@,#,$,%,^,&,*,?,_,~,\\s].*")) {
			special = true;
			intScore += 2;
		}

		// COMBOS
		if (upper && lower) // [verified] both upper and lower case
		{
			intScore += 1;
		}
		if ((upper || lower) && numbers) // [verified] both letters
		// and numbers
		{
			intScore += 1;
		}
		if ((upper || lower) && numbers && special) // [verified]
		// letters,
		// numbers,
		// and
		// special
		// characters
		{
			intScore += 1;
		}
		if (upper && lower && numbers && special) // [verified]
		// upper,
		// lower,
		// numbers,
		// and
		// special
		// characters
		{
			intScore += 1;
		}

		return intScore;
	}
}
