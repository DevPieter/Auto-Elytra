package nl.devpieter.autoelytra.Utils;

public class NumberUtils {

	public static int stringToInt(String input) {
		try {
			return Integer.parseInt(input);
		} catch (Exception e) {
			return -1;
		}
	}

	public static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
