package nl.devpieter.autoelytra.Enums;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import nl.devpieter.autoelytra.Utils.LogUtils;

public enum Messages {

	NO_ELYTRA("&cNo valid elytra found!", true, false),
	NO_CHESTPLATE("&cNo valid chestplate found!", true, false),
	NO_INVENTORY_SPACE("&cNo free inventory space was found!", true, false),

	EQUIP_TYPE_MANUAL("&aEquip type set to &6Manual&a.", false, true),
	EQUIP_TYPE_AUTOMATIC("&aEquip type set to &6Automatic&a.", false, true);

	public String message;
	public boolean usePrefix;
	public boolean actionBar;

	Messages(String message, boolean usePrefix, boolean actionBar) {
		this.message = message;
		this.actionBar = actionBar;
	}

	private PlayerEntity player = MinecraftClient.getInstance().player;

	public void sendMessage() {
		if (player == null || player.world == null)
			return;

		if (usePrefix)
			player.sendMessage(new LiteralText(translateColorCodes('&', String.format("&3[&dAuto Elytra&3]&r %s", message))), actionBar);
		else
			player.sendMessage(new LiteralText(translateColorCodes('&', message)), actionBar);

		LogUtils.logMessage(translateColorCodes('&', message));
	}

	public void sendMessage(boolean usePrefix, boolean actionBar) {
		if (player == null || player.world == null)
			return;

		if (usePrefix)
			player.sendMessage(new LiteralText(translateColorCodes('&', String.format("&3[&dAuto Elytra&3]&r %s", message))), actionBar);
		else
			player.sendMessage(new LiteralText(translateColorCodes('&', message)), actionBar);

		LogUtils.logMessage(translateColorCodes('&', message));
	}

	public String translateColorCodes(Character character, String message) {
		message = message.replaceAll(character + "4", "\u00A74");
		message = message.replaceAll(character + "c", "\u00A7c");
		message = message.replaceAll(character + "6", "\u00A76");
		message = message.replaceAll(character + "e", "\u00A7e");
		message = message.replaceAll(character + "2", "\u00A72");
		message = message.replaceAll(character + "a", "\u00A7a");
		message = message.replaceAll(character + "b", "\u00A7b");
		message = message.replaceAll(character + "3", "\u00A73");
		message = message.replaceAll(character + "1", "\u00A71");
		message = message.replaceAll(character + "9", "\u00A79");
		message = message.replaceAll(character + "d", "\u00A7d");
		message = message.replaceAll(character + "5", "\u00A75");
		message = message.replaceAll(character + "f", "\u00A7f");
		message = message.replaceAll(character + "7", "\u00A77");
		message = message.replaceAll(character + "8", "\u00A78");
		message = message.replaceAll(character + "0", "\u00A70");

		message = message.replaceAll(character + "k", "\u00A7k");
		message = message.replaceAll(character + "l", "\u00A7l");
		message = message.replaceAll(character + "m", "\u00A7m");
		message = message.replaceAll(character + "n", "\u00A7n");
		message = message.replaceAll(character + "o", "\u00A7o");
		message = message.replaceAll(character + "r", "\u00A7r");
		return message;
	}
}
