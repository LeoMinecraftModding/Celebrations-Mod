package team.leomc.celebrations.client.gui.screens;

import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import team.leomc.celebrations.block.entity.SignLikeBlockEntity;
import team.leomc.celebrations.util.SignLikeText;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class SignLikeEditScreen extends Screen {
	protected static final float SCALE = 1.5f;
	protected static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F * SCALE, 0.9765628F * SCALE, 0.9765628F * SCALE);
	protected final SignLikeBlockEntity signLike;
	protected SignLikeText text;
	public final String[] messages;
	protected int frame;
	public int line;
	public TextFieldHelper textField;

	public SignLikeEditScreen(Component title, SignLikeBlockEntity entity) {
		super(title);
		this.signLike = entity;
		this.text = signLike.getText();
		// this.messages = text.messages().stream().map(Component::getString).toArray(String[]::new);
		this.messages = new String[10]; // todo fix
		Arrays.fill(messages, "111");
	}

	@Override
	protected void init() {
		this.addRenderableWidget(
			Button.builder(CommonComponents.GUI_DONE, button -> this.onDone()).bounds(this.width / 2 - 100, this.height / 4 + 144, 200, 20).build()
		);
		this.textField = new TextFieldHelper(
			() -> this.messages[this.line],
			this::setMessage,
			TextFieldHelper.createClipboardGetter(this.minecraft),
			TextFieldHelper.createClipboardSetter(this.minecraft),
			string -> this.minecraft.font.width(string) <= this.signLike.getMaxTextLineWidth()
		);
	}

	public void tick() {
		++this.frame;
		if (!this.isValid()) {
			this.onDone();
		}
	}

	protected boolean isValid() {
		return this.minecraft != null && this.minecraft.player != null && !this.signLike.isRemoved() && this.signLike.getBlockPos().distToCenterSqr(this.minecraft.player.position()) < 16;
	}

	public boolean charTyped(char c, int i) {
		if (textField != null) {
			this.textField.charTyped(c);
		}
		return true;
	}

	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		Lighting.setupForFlatItems();
		graphics.drawCenteredString(this.font, this.title, this.width / 2, 40, 16777215);
		this.renderSignLike(graphics);
		Lighting.setupFor3DItems();
	}

	public void onClose() {
		this.onDone();
	}

	public boolean isPauseScreen() {
		return false;
	}

	protected void renderSignLike(GuiGraphics graphics) {
		graphics.pose().pushPose();
		this.offsetSignLike(graphics);
		graphics.pose().pushPose();
		this.renderSignLikeBackground(graphics);
		graphics.pose().popPose();
		this.renderSignLikeText(graphics);
		graphics.pose().popPose();
	}

	protected void renderSignLikeText(GuiGraphics graphics) {
		if (textField != null) {
			offsetSignLikeText(graphics);
			Vector3f vector3f = this.getSignLikeTextScale();
			graphics.pose().scale(vector3f.x(), vector3f.y(), vector3f.z());
			int i = this.text.color().getTextColor();
			boolean flag = this.frame / 6 % 2 == 0;
			int j = this.textField.getCursorPos();
			int k = this.textField.getSelectionPos();
			int l = 4 * this.signLike.getTextLineHeight() / 2;
			int i1 = this.line * this.signLike.getTextLineHeight() - l;

			for (int j1 = 0; j1 < this.messages.length; ++j1) {
				String s = this.messages[j1];
				if (s != null) {
					if (this.font.isBidirectional()) {
						s = this.font.bidirectionalShaping(s);
					}

					int k1 = -this.font.width(s) / 2;
					graphics.drawString(this.font, s, k1, j1 * this.signLike.getTextLineHeight() - l, i, false);
					if (j1 == this.line && j >= 0 && flag) {
						int l1 = this.font.width(s.substring(0, Math.max(Math.min(j, s.length()), 0)));
						int i2 = l1 - this.font.width(s) / 2;
						if (j >= s.length()) {
							graphics.drawString(this.font, "_", i2, i1, i, false);
						}
					}
				}
			}

			for (int k3 = 0; k3 < this.messages.length; ++k3) {
				String s1 = this.messages[k3];
				if (s1 != null && k3 == this.line && j >= 0) {
					int l3 = this.font.width(s1.substring(0, Math.max(Math.min(j, s1.length()), 0)));
					int i4 = l3 - this.font.width(s1) / 2;
					if (flag && j < s1.length()) {
						graphics.fill(i4, i1 - 1, i4 + 1, i1 + this.signLike.getTextLineHeight(), -16777216 | i);
					}

					if (k != j) {
						int j4 = Math.min(j, k);
						int j2 = Math.max(j, k);
						int k2 = this.font.width(s1.substring(0, j4)) - this.font.width(s1) / 2;
						int l2 = this.font.width(s1.substring(0, j2)) - this.font.width(s1) / 2;
						int i3 = Math.min(k2, l2);
						int j3 = Math.max(k2, l2);
						graphics.fill(RenderType.guiTextHighlight(), i3, i1, j3, i1 + this.signLike.getTextLineHeight(), -16776961);
					}
				}
			}
		}
	}

	protected void onDone() {
		this.minecraft.setScreen(null);
	}

	public void setMessage(String s) {
		this.messages[this.line] = s;
		this.text = this.text.setMessage(this.line, Component.literal(s));
		this.signLike.setText(this.text);
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (textField != null) {
			if (keyCode == 265) {
				this.line = Math.max(this.line - 1, 0);
				this.textField.setCursorToEnd();
				return true;
			} else if (keyCode != 264 && keyCode != 257 && keyCode != 335) {
				return this.textField.keyPressed(keyCode) || super.keyPressed(keyCode, scanCode, modifiers);
			} else {
				this.line = Math.min(this.line + 1, 6);
				this.textField.setCursorToEnd();
				return true;
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	protected void offsetSignLike(GuiGraphics graphics) {
		graphics.pose().translate((float) this.width / 2.0F, 90.0F, 50.0F);
	}

	protected void offsetSignLikeText(GuiGraphics graphics) {
		graphics.pose().translate(0.0F, 0.0F, 4.0F);
	}

	protected void renderSignLikeBackground(GuiGraphics graphics) {

	}

	protected Vector3f getSignLikeTextScale() {
		return TEXT_SCALE;
	}
}
