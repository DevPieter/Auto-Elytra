package nl.devpieter.autoelytra;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import nl.devpieter.autoelytra.Config.ConfigManager;
import nl.devpieter.autoelytra.Config.Settings;
import nl.devpieter.autoelytra.Enums.EquipType;
import nl.devpieter.autoelytra.Enums.Messages;
import nl.devpieter.autoelytra.Keybinding.KeybindingManager;
import nl.devpieter.autoelytra.Screen.SettingsScreen;

public class AutoElytra implements ModInitializer {

	private static ConfigManager configManager;
	private static ElytraManager elytraManager;
	private static KeybindingManager keybindingsManager;

	@Override
	public void onInitialize() {
		configManager = new ConfigManager("AutoElytra");

		configManager.addConfigSetting(Settings.EQUIP_TYPE.key, Settings.EQUIP_TYPE.defaultValue);
		configManager.addConfigSetting(Settings.BLOCK_HEIGHT.key, Settings.BLOCK_HEIGHT.defaultValue);

		configManager.addConfigSetting(Settings.IGNORE_WATER.key, Settings.IGNORE_WATER.defaultValue);
		configManager.addConfigSetting(Settings.IGNORE_LAVA.key, Settings.IGNORE_LAVA.defaultValue);

		configManager.addConfigSetting(Settings.EQUIP_ELYTRA_WHEN_IN_THE_AIR.key, Settings.EQUIP_ELYTRA_WHEN_IN_THE_AIR.defaultValue);
		configManager.addConfigSetting(Settings.EQUIP_ELYTRA_WHEN_HOLDING_FIREWORKS.key, Settings.EQUIP_ELYTRA_WHEN_HOLDING_FIREWORKS.defaultValue);
		configManager.addConfigSetting(Settings.PLAY_EQUIP_SOUND.key, Settings.PLAY_EQUIP_SOUND.defaultValue);

		configManager.addConfigSetting(Settings.SHOW_INVENTORY_FULL_WARNING.key, Settings.SHOW_INVENTORY_FULL_WARNING.defaultValue);

		configManager.createConfigFile(true);
		configManager.loadConfg();

		elytraManager = new ElytraManager(new Item[] { Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE }, configManager);
		keybindingsManager = new KeybindingManager();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			if (client != null && client.player != null && client.player.getInventory() != null)
				if (!elytraManager.checkIfValidInventory(client.player.getInventory()) && configManager.getBoolean(Settings.SHOW_INVENTORY_FULL_WARNING.key, false))
					Messages.NO_INVENTORY_SPACE.sendMessage(false, true);

			boolean manualEquipEnabled = ((EquipType) configManager.getEnum(Settings.EQUIP_TYPE.key, EquipType.class, false)).equals(EquipType.MANUAL);
			if (keybindingsManager.swap.wasPressed() && manualEquipEnabled)
				elytraManager.swap();
			if (keybindingsManager.equipElytra.wasPressed() && manualEquipEnabled)
				elytraManager.equipElytra();
			if (keybindingsManager.equipChestplate.wasPressed() && manualEquipEnabled)
				elytraManager.equipChestplate();

			if (keybindingsManager.changeSwapType.wasPressed())
				if (((EquipType) configManager.getEnum(Settings.EQUIP_TYPE.key, EquipType.class, false)).equals(EquipType.AUTOMATIC)) {
					configManager.setEnum(Settings.EQUIP_TYPE.key, EquipType.MANUAL, true);
					Messages.EQUIP_TYPE_MANUAL.sendMessage();
				} else if (((EquipType) configManager.getEnum(Settings.EQUIP_TYPE.key, EquipType.class, false)).equals(EquipType.MANUAL)) {
					configManager.setEnum(Settings.EQUIP_TYPE.key, EquipType.AUTOMATIC, true);
					Messages.EQUIP_TYPE_AUTOMATIC.sendMessage();
				}

			if (keybindingsManager.openSettingsScreen.wasPressed())
				client.setScreen(new SettingsScreen("Auto Elytra Settings"));
		});
	}

	public static ConfigManager getConfigManager() {
		return configManager;
	}

	public static ElytraManager getElytraManager() {
		return elytraManager;
	}
}