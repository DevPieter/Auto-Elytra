package nl.devpieter.autoelytra.Screen;

import java.awt.Color;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import nl.devpieter.autoelytra.AutoElytra;
import nl.devpieter.autoelytra.ElytraManager;
import nl.devpieter.autoelytra.Config.ConfigManager;
import nl.devpieter.autoelytra.Config.Settings;
import nl.devpieter.autoelytra.Enums.EquipType;
import nl.devpieter.autoelytra.Enums.Options.YesNoOptions;
import nl.devpieter.autoelytra.Utils.BooleanUtils;

public class SettingsScreen extends Screen {

	private ConfigManager configManager = AutoElytra.getConfigManager();
	private ElytraManager elytraManager = AutoElytra.getElytraManager();

	// Settings
	private boolean transparentBackground = false;
	private int borderThickness = 1;

	private int backgroundColor = new Color(25, 25, 28).getRGB();
	private int borderColor = new Color(69, 69, 69).getRGB();
	private int titleColor = new Color(222, 222, 222).getRGB();

	private boolean useRainbowBorder = true;
	private boolean useRainbowTitle = true;

	// Components
	private CyclingButtonWidget<EquipType> equipTypeButton;

	private SliderWidget blockHeightSlider;
	private CyclingButtonWidget<YesNoOptions> equipElytraWhenInTheAir;
	private CyclingButtonWidget<YesNoOptions> equipElytraWhenHoldingFireworks;

	private CyclingButtonWidget<YesNoOptions> ignoreWater;
	private CyclingButtonWidget<YesNoOptions> ignoreLava;

	private ButtonWidget equipElytraButton;
	private ButtonWidget swapButton;
	private ButtonWidget equipChestplateButton;

	// Values
	private int blockHeightValue = 0;

	public SettingsScreen(String title) {
		super(new LiteralText(title));
	}

	@Override
	protected void init() {
		this.blockHeightValue = configManager.getInteger(Settings.BLOCK_HEIGHT.key, false);

		// ---
		this.equipTypeButton = (CyclingButtonWidget<EquipType>) this.addDrawableChild(CyclingButtonWidget.builder(EquipType::getNameText).values(EquipType.values()).initially((EquipType) configManager.getEnum(Settings.EQUIP_TYPE.key, EquipType.class, false)).build(getPositionX(200), getPositionY(0), 200, 20, new LiteralText("Equip Type"), (button, equipType) -> {
			configManager.setEnum(Settings.EQUIP_TYPE.key, equipType, true);
		}));

		// ---
		this.blockHeightSlider = (SliderWidget) this.addDrawableChild(new SliderWidget(getPositionX(200), getPositionY(2), 200, 20, new LiteralText("Block Height: " + blockHeightValue), (double) blockHeightValue / 6) {

			@Override
			protected void updateMessage() {
				setMessage(new LiteralText("Block Height: " + blockHeightValue));
			}

			@Override
			protected void applyValue() {
				int newValue = (int) Math.round(value * 4 + 1);
				if (newValue != blockHeightValue) {
					blockHeightValue = newValue;
					configManager.setValue(Settings.BLOCK_HEIGHT.key, blockHeightValue, true);
				}
			}
		});

		this.equipElytraWhenInTheAir = (CyclingButtonWidget<YesNoOptions>) this.addDrawableChild(CyclingButtonWidget.builder(YesNoOptions::getText).values(YesNoOptions.values()).initially(BooleanUtils.toYesNoOptions(configManager.getBoolean(Settings.EQUIP_ELYTRA_WHEN_IN_THE_AIR.key, false))).build(getPositionX(200), getPositionY(3), 200, 20, new LiteralText("Equip When In The Air"), (button, yesNoOptions) -> {
			configManager.setValue(Settings.EQUIP_ELYTRA_WHEN_IN_THE_AIR.key, BooleanUtils.fromYesNoOptions(yesNoOptions), true);
		}));

		this.equipElytraWhenHoldingFireworks = (CyclingButtonWidget<YesNoOptions>) this.addDrawableChild(CyclingButtonWidget.builder(YesNoOptions::getText).values(YesNoOptions.values()).initially(BooleanUtils.toYesNoOptions(configManager.getBoolean(Settings.EQUIP_ELYTRA_WHEN_HOLDING_FIREWORKS.key, false))).build(getPositionX(200), getPositionY(4), 200, 20, new LiteralText("Equip When Holding Fireworks"), (button, yesNoOptions) -> {
			configManager.setValue(Settings.EQUIP_ELYTRA_WHEN_HOLDING_FIREWORKS.key, BooleanUtils.fromYesNoOptions(yesNoOptions), true);
		}));

		this.ignoreWater = (CyclingButtonWidget<YesNoOptions>) this.addDrawableChild(CyclingButtonWidget.builder(YesNoOptions::getText).values(YesNoOptions.values()).initially(BooleanUtils.toYesNoOptions(configManager.getBoolean(Settings.IGNORE_WATER.key, false))).build(getPositionX(200), getPositionY(6), 200, 20, new LiteralText("Ignore Water"), (button, yesNoOptions) -> {
			configManager.setValue(Settings.IGNORE_WATER.key, BooleanUtils.fromYesNoOptions(yesNoOptions), true);
		}));

		this.ignoreLava = (CyclingButtonWidget<YesNoOptions>) this.addDrawableChild(CyclingButtonWidget.builder(YesNoOptions::getText).values(YesNoOptions.values()).initially(BooleanUtils.toYesNoOptions(configManager.getBoolean(Settings.IGNORE_LAVA.key, false))).build(getPositionX(200), getPositionY(7), 200, 20, new LiteralText("Ignore Lava"), (button, yesNoOptions) -> {
			configManager.setValue(Settings.IGNORE_LAVA.key, BooleanUtils.fromYesNoOptions(yesNoOptions), true);
		}));

		// ---
		this.equipElytraButton = (ButtonWidget) this.addDrawableChild(new ButtonWidget((this.width / 2) - 151, this.height - 23, 100, 20, new LiteralText("Equip Elytra"), (button) -> {
			elytraManager.equipElytra();
		}));

		this.swapButton = (ButtonWidget) this.addDrawableChild(new ButtonWidget((this.width / 2) - 50, this.height - 23, 100, 20, new LiteralText("Swap"), (button) -> {
			elytraManager.swap();
		}));

		this.equipChestplateButton = (ButtonWidget) this.addDrawableChild(new ButtonWidget((this.width / 2) + 51, this.height - 23, 100, 20, new LiteralText("Equip Chestplate"), (button) -> {
			elytraManager.equipChestplate();
		}));

		// ---
		this.addDrawableChild(new PlayerModelWidget(this.width - 50, this.height - 63, 30, client.player));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

		boolean enabled = !equipTypeButton.getValue().equals(EquipType.AUTOMATIC);
		equipElytraButton.active = enabled;
		swapButton.active = enabled;
		equipChestplateButton.active = enabled;

		blockHeightSlider.active = !enabled;
		equipElytraWhenInTheAir.active = !enabled;
		equipElytraWhenHoldingFireworks.active = !enabled;
		ignoreWater.active = !enabled;
		ignoreLava.active = !enabled;

		// Draw Background
		if (transparentBackground)
			renderBackground(matrices);
		else
			fill(matrices, 0, 0, this.width, this.height, backgroundColor);

		// Draw Vertical Border
		fill(matrices, 0, 0, this.width + 1, 0 + borderThickness, getColor(borderColor, useRainbowBorder));
		fill(matrices, 0, this.height, this.width + 1, this.height - borderThickness, getColor(borderColor, useRainbowBorder));

		// Draw Horizontal Border
		fill(matrices, 0, 0 + 1, 0 + borderThickness, this.height, getColor(borderColor, useRainbowBorder));
		fill(matrices, this.width, 0 + 1, this.width - borderThickness, this.height, getColor(borderColor, useRainbowBorder));

		// Draw Title
		drawCenteredText(matrices, textRenderer, title, this.width / 2, 5, getColor(titleColor, useRainbowTitle));

		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private int getPositionX(int size) {
		return (this.width - size) / 2;
	}

	private int getPositionY(int index) {
		return (21 * index) + 50;
	}

	private int getColor(int color, boolean rainbow) {
		if (rainbow) {
			int rainbowSpeed = 5;
			float hue = (System.currentTimeMillis() % (int) (rainbowSpeed * 1000)) / (float) (rainbowSpeed * 1000);
			return Color.HSBtoRGB(hue, 1, 1);
		} else
			return color;
	}
}
