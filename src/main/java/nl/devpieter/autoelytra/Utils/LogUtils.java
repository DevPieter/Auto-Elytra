package nl.devpieter.autoelytra.Utils;

public class LogUtils {

	private static String PREFIX = "[Auto Elytra]";

	public static void logMessage(String message) {
		System.out.println(String.format("%s %s", PREFIX, message));
	}

	public static void logInfo(String message) {
		System.out.println(String.format("%s %s %s", PREFIX, "[Info]", message));
	}

	public static void logWarning(String message) {
		System.err.println(String.format("%s %s %s", PREFIX, "[Warning]", message));
	}

	public static void logError(String message) {
		System.out.println(String.format("%s %s %s", PREFIX, "[Error]", message));
	}
}
