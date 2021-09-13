package nl.devpieter.autoelytra.Config;

import nl.devpieter.autoelytra.Enums.EquipType;

public enum Settings {

	EQUIP_TYPE("Equip Type", "The way the mod puts on your armor.", "equip_type", EquipType.AUTOMATIC.toString()),
	BLOCK_HEIGHT("Block Height", "The number of blocks below you that must be air to equip your Elytra.", "block_height", "1");

	public final String name;
	public final String description;
	public final String key;
	public final String defaultValue;
	public String value;

	Settings(String name, String description, String key, String value) {
		this.name = name;
		this.description = description;
		this.key = key;
		this.defaultValue = value;
		this.value = value;
	}
}
