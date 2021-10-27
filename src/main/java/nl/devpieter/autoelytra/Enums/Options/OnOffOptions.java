package nl.devpieter.autoelytra.Enums.Options;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public enum OnOffOptions {

	ON("On"), OFF("Off");

	public Text text;

	private OnOffOptions(String text) {
		this.text = new LiteralText(text);
	}

	public Text getText() {
		return text;
	}
}
