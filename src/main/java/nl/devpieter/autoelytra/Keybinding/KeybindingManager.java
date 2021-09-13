package nl.devpieter.autoelytra.Keybinding;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeybindingManager {
	private final String categoryName = "Auto Elytra";
	public final KeyBinding swap = KeyBindingHelper.registerKeyBinding(new KeyBinding("Swap", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, categoryName));
	public final KeyBinding equipElytra = KeyBindingHelper.registerKeyBinding(new KeyBinding("Equip Elytra", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, categoryName));
	public final KeyBinding equipChestplate = KeyBindingHelper.registerKeyBinding(new KeyBinding("Equip Chestplate", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, categoryName));
	public final KeyBinding changeSwapType = KeyBindingHelper.registerKeyBinding(new KeyBinding("Change Swap Type", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, categoryName));
}
