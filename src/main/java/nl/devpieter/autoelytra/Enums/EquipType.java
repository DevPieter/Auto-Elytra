package nl.devpieter.autoelytra.Enums;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public enum EquipType {
	MANUAL("Manual"), AUTOMATIC("Automatic");

	public final String name;

	private EquipType(String name) {
		this.name = name;
	}

	public Text getNameText() {
		return new LiteralText(name);
	}
}
