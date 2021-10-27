package nl.devpieter.autoelytra.Mixin;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import nl.devpieter.autoelytra.AutoElytra;
import nl.devpieter.autoelytra.Config.ConfigManager;
import nl.devpieter.autoelytra.Config.Settings;
import nl.devpieter.autoelytra.Enums.EquipType;

@Mixin(ClientWorld.class)
public abstract class PlayerEntityMixin extends World {

	MinecraftClient client = MinecraftClient.getInstance();

	protected PlayerEntityMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
		super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	@Environment(EnvType.CLIENT)
	private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
		if (((EquipType) AutoElytra.getConfigManager().getEnum(Settings.EQUIP_TYPE.key, EquipType.class, false)).equals(EquipType.AUTOMATIC)) {

			ConfigManager configManager = AutoElytra.getConfigManager();

			boolean equipElytraWhenInTheAir = configManager.getBoolean(Settings.EQUIP_ELYTRA_WHEN_IN_THE_AIR.key, false);
			boolean equipElytraWhenHoldingFireworks = configManager.getBoolean(Settings.EQUIP_ELYTRA_WHEN_HOLDING_FIREWORKS.key, false);

			PlayerEntity player = client.player;

			ItemStack mainHand = player.getInventory().getMainHandStack();
			ItemStack offHand = player.getInventory().getStack(40);

			boolean isInTheAir = true;
			boolean isHoldingFireworkRocket = mainHand.getItem().equals(Items.FIREWORK_ROCKET) || offHand.getItem().equals(Items.FIREWORK_ROCKET);

			if (equipElytraWhenHoldingFireworks && isHoldingFireworkRocket)
				AutoElytra.getElytraManager().equipElytra();
			else if (equipElytraWhenHoldingFireworks && !isHoldingFireworkRocket && player.isOnGround())
				AutoElytra.getElytraManager().equipChestplate();
			else if (equipElytraWhenInTheAir) {
				for (int i = 1; i <= configManager.getInteger(Settings.BLOCK_HEIGHT.key, false); i++) {
					BlockState block = getBlockState(player.getBlockPos().add(new Vec3i(0, -i, 0)));
					if (!(block.getBlock() instanceof AirBlock || block.getBlock() instanceof PlantBlock || block.getBlock() instanceof FluidBlock))
						isInTheAir = false;

					if (!configManager.getBoolean(Settings.IGNORE_WATER.key, false) && block.getBlock().equals(Blocks.WATER))
						isInTheAir = false;

					if (!configManager.getBoolean(Settings.IGNORE_LAVA.key, false) && block.getBlock().equals(Blocks.LAVA))
						isInTheAir = false;
				}

				if (!player.isOnGround() && isInTheAir) {
					if (!player.getInventory().getStack(38).getItem().equals(Items.ELYTRA))
						AutoElytra.getElytraManager().equipElytra();
				} else if (!player.isFallFlying()) {
					if (player.getInventory().getStack(38).getItem().equals(Items.ELYTRA))
						AutoElytra.getElytraManager().equipChestplate();
				}
			}
		}
	}
}
