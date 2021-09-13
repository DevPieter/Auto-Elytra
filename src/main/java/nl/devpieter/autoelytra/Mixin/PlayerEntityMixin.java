package nl.devpieter.autoelytra.Mixin;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import nl.devpieter.autoelytra.AutoElytra;
import nl.devpieter.autoelytra.Config.Settings;
import nl.devpieter.autoelytra.Enums.EquipType;
import nl.devpieter.autoelytra.Utils.NumberUtils;

@Mixin(ClientWorld.class)
public abstract class PlayerEntityMixin extends World {

	MinecraftClient client = MinecraftClient.getInstance();

	protected PlayerEntityMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
		super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
		if (Settings.EQUIP_TYPE.value.equalsIgnoreCase(EquipType.AUTOMATIC.toString())) {

			boolean air = true;
			PlayerEntity player = client.player;

			for (int i = 1; i <= NumberUtils.stringToInt(Settings.BLOCK_HEIGHT.value); i++) {
				BlockState block = getBlockState(player.getBlockPos().add(new Vec3i(0, -i, 0)));
				if (!(block.getBlock() instanceof AirBlock || block.getBlock() instanceof PlantBlock || block.getBlock() instanceof FluidBlock))
					air = false;
			}

			if (!player.isOnGround() && air)
				AutoElytra.getElytraManager().equipElytra();
			else if (!player.isFallFlying())
				AutoElytra.getElytraManager().equipChestplate();
		}
	}
}
