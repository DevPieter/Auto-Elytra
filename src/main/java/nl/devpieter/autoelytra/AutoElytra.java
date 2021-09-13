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

public class AutoElytra implements ModInitializer {

	private static ConfigManager configManager = new ConfigManager();
	private static ElytraManager elytraManager = new ElytraManager(new Item[] { Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE });;
	private static KeybindingManager keybindingsManager = new KeybindingManager();

	@Override
	public void onInitialize() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			if (Settings.EQUIP_TYPE.value.equalsIgnoreCase(EquipType.MANUAL.toString())) {
				if (keybindingsManager.swap.wasPressed())
					elytraManager.swap();
				if (keybindingsManager.equipElytra.wasPressed())
					elytraManager.equipElytra();
				if (keybindingsManager.equipChestplate.wasPressed())
					elytraManager.equipChestplate();
			}

			if (keybindingsManager.changeSwapType.wasPressed())
				if (Settings.EQUIP_TYPE.value.equalsIgnoreCase(EquipType.AUTOMATIC.toString())) {
					configManager.setValue(Settings.EQUIP_TYPE, EquipType.MANUAL.toString(), true);
					Messages.EQUIP_TYPE_MANUAL.sendMessage();
				} else if (Settings.EQUIP_TYPE.value.equalsIgnoreCase(EquipType.MANUAL.toString())) {
					configManager.setValue(Settings.EQUIP_TYPE, EquipType.AUTOMATIC.toString(), true);
					Messages.EQUIP_TYPE_AUTOMATIC.sendMessage();
				}
		});
	}

	public static ConfigManager getConfigManager() {
		return configManager;
	}

	public static ElytraManager getElytraManager() {
		return elytraManager;
	}
}
