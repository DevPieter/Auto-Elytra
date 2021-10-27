package nl.devpieter.autoelytra.Enums.Options;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public enum YesNoOptions {

	YES("Yes"), NO("No");

	public Text text;

	private YesNoOptions(String text) {
		this.text = new LiteralText(text);
	}

	public Text getText() {
		return text;
	}
}
