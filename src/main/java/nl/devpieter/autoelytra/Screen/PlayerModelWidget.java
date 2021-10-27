package nl.devpieter.autoelytra.Screen;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class PlayerModelWidget extends DrawableHelper implements Drawable, Element, Selectable {

	private boolean renderDebugBox = false;

	public PlayerEntity player;
	public int size = 30;
	public int rotation = 0;
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	protected boolean hovered;
	public boolean active = true;
	public boolean visible = true;

	public PlayerModelWidget(int x, int y, int size, PlayerEntity player) {
		this.player = player;
		this.size = size;

		this.x1 = x;
		this.y1 = y;
		this.x2 = x + this.size + 15;
		this.y2 = y + this.size * 2;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (this.visible) {
			this.hovered = mouseX >= this.x1 && mouseY >= this.y1 && mouseX <= this.x2 && mouseY <= this.y2;

			if (renderDebugBox)
				fill(matrices, x1, y1, x2, y2, new Color(222, 222, 222).getRGB());

			MatrixStack matrixStack = RenderSystem.getModelViewStack();
			matrixStack.push();
			matrixStack.translate((double) (x1 + x2) / 2, (double) y2, 1050.0D);
			matrixStack.scale(1.0F, 1.0F, -1.0F);
			RenderSystem.applyModelViewMatrix();
			MatrixStack matrixStack2 = new MatrixStack();
			matrixStack2.translate(0.0D, 0.0D, 1000.0D);
			matrixStack2.scale((float) size, (float) size, (float) size);
			Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
			Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(0.0F);
			quaternion.hamiltonProduct(quaternion2);
			matrixStack2.multiply(quaternion);

			float bodyYaw = player.bodyYaw;
			float yaw = player.getYaw();
			float pitch = player.getPitch();
			float prevHeadYaw = player.prevHeadYaw;
			float headYaw = player.headYaw;

			player.bodyYaw = 200.0F + rotation;
			player.setYaw(180.0F + rotation);
			player.setPitch(0.0F);
			player.headYaw = player.getYaw();
			player.prevHeadYaw = player.getYaw();

			DiffuseLighting.method_34742();
			EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
			quaternion2.conjugate();
			entityRenderDispatcher.setRotation(quaternion2);
			entityRenderDispatcher.setRenderShadows(false);
			VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
			RenderSystem.runAsFancy(() -> {
				entityRenderDispatcher.render(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack2, immediate, 15728880);
			});

			immediate.draw();
			entityRenderDispatcher.setRenderShadows(true);

			player.bodyYaw = bodyYaw;
			player.setYaw(yaw);
			player.setPitch(pitch);
			player.prevHeadYaw = prevHeadYaw;
			player.headYaw = headYaw;

			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();
			DiffuseLighting.enableGuiDepthLighting();
		}
	}

	public void onClick(double mouseX, double mouseY) {
	}

	public void onRelease(double mouseX, double mouseY) {
	}

	protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY, int button) {
		if (this.active && this.visible)
			if (isValidClickButton(button)) {
				int delta = (int) Math.round(deltaX);

				if (deltaX > 0) {
					rotation -= delta + 2;
				} else if (deltaX < 0) {
					rotation -= delta - 2;
				}
			}
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.active && this.visible) {
			if (this.isValidClickButton(button)) {
				boolean bl = this.clicked(mouseX, mouseY);
				if (bl) {
					this.onClick(mouseX, mouseY);
					return true;
				}
			}

			return false;
		} else {
			return false;
		}
	}

	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.isValidClickButton(button)) {
			this.onRelease(mouseX, mouseY);
			return true;
		} else {
			return false;
		}
	}

	protected boolean isValidClickButton(int button) {
		return button == 0;
	}

	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.isValidClickButton(button)) {
			this.onDrag(mouseX, mouseY, deltaX, deltaY, button);
			return true;
		} else {
			return false;
		}
	}

	protected boolean clicked(double mouseX, double mouseY) {
		return this.active && this.visible && mouseX >= this.x1 && mouseY >= this.y1 && mouseX <= this.x2 && mouseY <= this.y2;
	}

	public void playDownSound(SoundManager soundManager) {
		soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	public void renderDebugBox(boolean renderDebugBox) {
		this.renderDebugBox = renderDebugBox;
	}

	public boolean isHovered() {
		return this.hovered;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return this.active && this.visible && mouseX >= (double) this.x1 && mouseY >= (double) this.y1 && mouseX < (double) (this.x1 + this.x2) && mouseY < (double) (this.y1 + this.y2);
	}

	@Override
	public SelectionType getType() {
		return this.hovered ? Selectable.SelectionType.HOVERED : Selectable.SelectionType.NONE;
	}
}
