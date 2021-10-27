package nl.devpieter.autoelytra.Config;

import nl.devpieter.autoelytra.Enums.EquipType;

public enum Settings {

	EQUIP_TYPE("Equip Type", "equip_type", EquipType.AUTOMATIC),
	IGNORE_WATER("Ignore Water", "ignore_water", true),
	IGNORE_LAVA("Ignore Lava", "ignore_lava", false),
	BLOCK_HEIGHT("Block Height", "block_height", 1),
	EQUIP_ELYTRA_WHEN_IN_THE_AIR("Equip Elytra When In The Air", "equip_elytra_when_in_the_air", true),
	EQUIP_ELYTRA_WHEN_HOLDING_FIREWORKS("Equip Elytra When Holding Fireworks", "equip_elytra_when_holding_fireworks", true),
	PLAY_EQUIP_SOUND("Play Equip Sound", "play_equip_sound", true),
	SHOW_INVENTORY_FULL_WARNING("Show Inventory Full Warning", "show_inventory_full_warning", true);

	public final String name;
	public final String key;
	public final Object defaultValue;

	Settings(String name, String key, Object value) {
		this.name = name;
		this.key = key;
		this.defaultValue = value;
	}
}
