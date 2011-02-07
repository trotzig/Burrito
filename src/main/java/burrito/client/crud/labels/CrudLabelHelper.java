package burrito.client.crud.labels;

import java.util.MissingResourceException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;

public class CrudLabelHelper {

	private static Dictionary labels = Dictionary.getDictionary("BurritoMessages");

	/**
	 * This method will get a string from CrudLabels and if none is found, a
	 * {@link MissingResourceException} is thrown
	 * 
	 * @param methodName
	 * @return
	 */
	public static String getRequiredString(String methodName,
			String... fallbackMethodName) throws MissingResourceException {
		MissingResourceException mre = null;
		try {
			return labels.get(methodName);
		} catch (MissingResourceException e) {
			// expected, try fallbacks
			mre = e;
		}
		for (String m : fallbackMethodName) {
			try {
				return labels.get(m);
			} catch (MissingResourceException e) {
				// expected, try next fallback
				mre = e;
			}
		}
		throw mre;
	}

	/**
	 * This method is the failsafe equivalent of getRequiredString. If neither
	 * methodName nor any of the fallbacks are found, the methodName is
	 * returned, surrounded by {}, e.g. " {startDate}". An info log is logged,
	 * informing about how to correct the missing resource.
	 * 
	 * @param methodName
	 * @param fallbackMethodNames
	 */
	public static String getString(String methodName, String... fallbackMethodNames) {
		try {
			return getRequiredString(methodName, fallbackMethodNames);
		} catch (MissingResourceException e) {
			// warn about missing resource and return field name as fallback
			// default.
			StringBuilder sbMethods = new StringBuilder();
			for (String m : fallbackMethodNames) {
				sbMethods.append("\n...or...\nString ");
				sbMethods.append(m + "();");
			}
			StringBuilder sbFields = new StringBuilder();
			for (String m : fallbackMethodNames) {
				sbFields.append("\n...or...\n");
				sbFields.append(m + "=...");
			}

			GWT
					.log(
							"Warning: label is missing for field \""
									+ methodName
									+ "\". You should add the following lines of code to CrudLabels.java and CrudLabels.properties:"
									+ "\nString " + methodName + "();"
									+ sbMethods.toString()
									+ "\n" + methodName + "=..."
									+ sbFields.toString(), null);
			return "{" + methodName + "}";
		}
	}

	/**
	 * This method is similar to getString() but instead of returning a default
	 * value, <code>null</code> is returned and nothing is logged.
	 * 
	 * @param fullFieldName
	 * @param fallback
	 * @return
	 */
	public static String getNullableString(String methodName, String... fallbackMethodNames) {
		try {
			return getRequiredString(methodName, fallbackMethodNames);
		} catch (MissingResourceException e) {
			return null;
		}
	}

}
