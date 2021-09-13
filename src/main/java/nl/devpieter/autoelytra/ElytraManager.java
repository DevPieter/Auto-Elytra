package nl.devpieter.autoelytra;

import java.util.Arrays;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import nl.devpieter.autoelytra.Config.Settings;
import nl.devpieter.autoelytra.Enums.EquipType;
import nl.devpieter.autoelytra.Enums.Messages;

public class ElytraManager {

	private final MinecraftClient client;
	public final Item[] armorList;
	public final int MIN = -999;
	public final int INVENTORY_SIZE = 36;

	public ElytraManager(Item[] armorList) {
		this.client = MinecraftClient.getInstance();
		this.armorList = armorList;
	}

	public void swap() {
		PlayerEntity player = client.player;
		PlayerInventory inventory = player.getInventory();

		if (!checkIfValidInventory(inventory))
			return;

		int elytraSlot = getElytraSlot(inventory);
		if (elytraSlot == MIN && !player.getInventory().getStack(38).isOf(Items.ELYTRA))
			if (Settings.EQUIP_TYPE.value.equalsIgnoreCase(EquipType.MANUAL.toString()))
				Messages.NO_ELYTRA.sendMessage();

		int chestplateSlot = getChestplateSlot(inventory);
		if (chestplateSlot == MIN && !(player.getInventory().getStack(38).getItem() instanceof ArmorItem))
			if (Settings.EQUIP_TYPE.value.equalsIgnoreCase(EquipType.MANUAL.toString()))
				Messages.NO_CHESTPLATE.sendMessage();

		if (elytraSlot != MIN && player.getInventory().getStack(38).getItem().equals(Items.AIR)) {
			client.interactionManager.clickSlot(0, elytraSlot, 0, SlotActionType.QUICK_MOVE, client.player);
		} else if (chestplateSlot != MIN && player.getInventory().getStack(38).isOf(Items.AIR)) {
			client.interactionManager.clickSlot(0, chestplateSlot, 0, SlotActionType.QUICK_MOVE, client.player);
		} else if (chestplateSlot != MIN && player.getInventory().getStack(38).isOf(Items.ELYTRA)) {
			client.interactionManager.clickSlot(0, 6, 0, SlotActionType.QUICK_MOVE, client.player);
			client.interactionManager.clickSlot(0, chestplateSlot, 0, SlotActionType.QUICK_MOVE, client.player);
		} else if (elytraSlot != MIN && (player.getInventory().getStack(38).getItem() instanceof ArmorItem)) {
			client.interactionManager.clickSlot(0, 6, 0, SlotActionType.QUICK_MOVE, client.player);
			client.interactionManager.clickSlot(0, elytraSlot, 0, SlotActionType.QUICK_MOVE, client.player);
		}
	}

	public void equipChestplate() {
		PlayerEntity player = client.player;
		PlayerInventory inventory = player.getInventory();

		if (!checkIfValidInventory(inventory))
			return;

		int chestplateSlot = getChestplateSlot(inventory);
		if (chestplateSlot == MIN && !(player.getInventory().getStack(38).getItem() instanceof ArmorItem))
			if (Settings.EQUIP_TYPE.value.equalsIgnoreCase(EquipType.MANUAL.toString()))
				Messages.NO_CHESTPLATE.sendMessage();

		if (chestplateSlot != MIN && player.getInventory().getStack(38).isOf(Items.AIR)) {
			client.interactionManager.clickSlot(0, chestplateSlot, 0, SlotActionType.QUICK_MOVE, client.player);
		} else if (chestplateSlot != MIN && player.getInventory().getStack(38).isOf(Items.ELYTRA)) {
			client.interactionManager.clickSlot(0, 6, 0, SlotActionType.QUICK_MOVE, client.player);
			client.interactionManager.clickSlot(0, chestplateSlot, 0, SlotActionType.QUICK_MOVE, client.player);
		}
	}

	public void equipElytra() {
		PlayerEntity player = client.player;
		PlayerInventory inventory = player.getInventory();

		if (!checkIfValidInventory(inventory))
			return;

		int elytraSlot = getElytraSlot(inventory);
		if (elytraSlot == MIN && !player.getInventory().getStack(38).isOf(Items.ELYTRA))
			if (Settings.EQUIP_TYPE.value.equalsIgnoreCase(EquipType.MANUAL.toString()))
				Messages.NO_ELYTRA.sendMessage();

		if (elytraSlot != MIN && player.getInventory().getStack(38).isOf(Items.AIR)) {
			client.interactionManager.clickSlot(0, elytraSlot, 0, SlotActionType.QUICK_MOVE, client.player);
		} else if (elytraSlot != MIN && (player.getInventory().getStack(38).getItem() instanceof ArmorItem)) {
			client.interactionManager.clickSlot(0, 6, 0, SlotActionType.QUICK_MOVE, client.player);
			client.interactionManager.clickSlot(0, elytraSlot, 0, SlotActionType.QUICK_MOVE, client.player);
		}
	}

	private boolean checkIfValidInventory(PlayerInventory inventory) {
		if (inventory.getEmptySlot() == -1) {
			if (Settings.EQUIP_TYPE.value.equalsIgnoreCase(EquipType.MANUAL.toString()))
				Messages.NO_INVENTORY_SPACE.sendMessage();
			return false;
		}
		return true;
	}

	public int getElytraSlot(Inventory inventory) {
		int bestSlot = MIN;
		int bestLevel = MIN;

		for (int i = 0; i <= INVENTORY_SIZE; i++) {
			ItemStack item = inventory.getStack(i);

			if (item.isEmpty() || !item.isOf(Items.ELYTRA))
				continue;

			int level = getElytraLevel(item);
			if (!(level > bestLevel))
				continue;

			bestSlot = i;
			bestLevel = level;
		}

		if (bestSlot == MIN)
			return MIN;

		if (bestSlot < 9)
			bestSlot += 36;

		return bestSlot;
	}

	public int getElytraLevel(ItemStack item) {
		if (EnchantmentHelper.getLevel(Enchantments.BINDING_CURSE, item) > 0)
			return MIN;

		int level = 0;
		level += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, item) * 2;
		level += EnchantmentHelper.getLevel(Enchantments.MENDING, item);

		int damagePercentage = (item.getDamage() * 100) / item.getMaxDamage();
		if (damagePercentage >= 95)
			level = MIN;
		else if (damagePercentage >= 90)
			level -= 6;
		else if (damagePercentage >= 80)
			level -= 5;
		else if (damagePercentage >= 70)
			level -= 4;
		else if (damagePercentage >= 60)
			level -= 3;
		else if (damagePercentage >= 50)
			level -= 2;
		return level;
	}

	public int getChestplateSlot(Inventory inventory) {
		int bestSlot = MIN;
		int bestLevel = MIN;

		for (int i = 0; i <= INVENTORY_SIZE; i++) {
			ItemStack item = inventory.getStack(i);

			if (item.isEmpty() || !(item.getItem() instanceof ArmorItem) || !(Arrays.asList(armorList).contains(item.getItem())))
				continue;

			int level = getChestplateLevel((ArmorItem) item.getItem(), item);
			if (!(level > bestLevel))
				continue;

			bestSlot = i;
			bestLevel = level;
		}

		if (bestSlot == MIN)
			return MIN;

		if (bestSlot < 9)
			bestSlot += 36;

		return bestSlot;
	}

	public int getChestplateLevel(ArmorItem armor, ItemStack item) {
		if (EnchantmentHelper.getLevel(Enchantments.BINDING_CURSE, item) > 0)
			return MIN;

		Enchantment protection = Enchantments.PROTECTION;

		int level = 0;
		level += armor.getProtection() * 7;
		level += (int) armor.getToughness() * 5;
		level += protection.getProtectionAmount(EnchantmentHelper.getLevel(protection, item), DamageSource.player(client.player)) * 7;
		level += EnchantmentHelper.getLevel(Enchantments.THORNS, item) * 2;
		level += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, item);
		level += EnchantmentHelper.getLevel(Enchantments.MENDING, item);
		return level;
	}
}
