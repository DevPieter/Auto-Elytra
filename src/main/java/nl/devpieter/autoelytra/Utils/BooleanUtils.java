package nl.devpieter.autoelytra.Utils;

import nl.devpieter.autoelytra.Enums.Options.OnOffOptions;
import nl.devpieter.autoelytra.Enums.Options.YesNoOptions;

public class BooleanUtils {

	public static YesNoOptions toYesNoOptions(boolean input) {
		if (input)
			return YesNoOptions.YES;
		else
			return YesNoOptions.NO;
	}

	public static OnOffOptions toOnOffOptions(boolean input) {
		if (input)
			return OnOffOptions.ON;
		else
			return OnOffOptions.OFF;
	}

	public static boolean fromYesNoOptions(YesNoOptions options) {
		if (options.equals(YesNoOptions.YES))
			return true;
		else
			return false;
	}

	public static boolean fromOnOffOptions(OnOffOptions options) {
		if (options.equals(OnOffOptions.ON))
			return true;
		else
			return false;
	}

	public static boolean fromString(String input) {
		if (input.equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}

	public static boolean fromInteger(int input) {
		if (input == 1)
			return true;
		else
			return false;
	}
}
