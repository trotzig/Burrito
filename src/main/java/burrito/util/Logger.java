package burrito.util;

public class Logger {

	private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());
	
	public static void error(String message) {
		logger.severe(message);
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void warn(String string) {
		logger.warning(string);
	}

}
