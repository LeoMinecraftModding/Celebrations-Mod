package cn.leolezury.celebrations.client.gui.screens;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.block.entity.CoupletBlockEntity;
import cn.leolezury.celebrations.block.entity.CoupletText;
import cn.leolezury.celebrations.client.renderer.CoupletRenderer;
import cn.leolezury.celebrations.message.CoupletUpdateMessage;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.stream.IntStream;

@OnlyIn(Dist.CLIENT)
public class CoupletEditScreen extends Screen {
    private static final float scale = 1.5f;
    private static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F * scale, 0.9765628F * scale, 0.9765628F * scale);
    @Nullable
    private CoupletRenderer.CoupletModel coupletModel;
    private final CoupletBlockEntity couplet;
    private CoupletText text;
    public final String[] messages;
    private int frame;
    public int line;
    @Nullable
    public TextFieldHelper coupletField;
    
    public CoupletEditScreen(CoupletBlockEntity entity, boolean b1) {
        super(Component.translatable("tooltip." + Celebrations.MOD_ID + ".edit_couplet"));
        this.couplet = entity;
        this.text = couplet.getFrontText();
        this.messages = IntStream.range(0, 7).mapToObj((i) -> {
            return text.getMessage(i, b1);
        }).map(Component::getString).toArray((i) -> {
            return new String[i];
        });
    }

    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_251194_) -> {
            this.onDone();
        }).bounds(this.width / 2 - 100, this.height / 4 + 144, 200, 20).build());
        this.coupletField = new TextFieldHelper(() -> {
            return this.messages[this.line];
        }, this::setMessage, TextFieldHelper.createClipboardGetter(this.minecraft), TextFieldHelper.createClipboardSetter(this.minecraft), (s) -> {
            return s.length() <= 1;
        });
        this.coupletModel = CoupletRenderer.createCoupletModel(this.minecraft.getEntityModels());
    }

    public void tick() {
        ++this.frame;
        if (!this.isValid()) {
            this.onDone();
        }
    }

    private boolean isValid() {
        return this.minecraft != null && this.minecraft.player != null && !this.couplet.isRemoved() && !this.couplet.playerIsTooFarAwayToEdit(this.minecraft.player.getUUID());
    }

    public boolean charTyped(char c, int i) {
        this.coupletField.charTyped(c);
        return true;
    }

    public void render(GuiGraphics graphics, int i, int i1, float v) {
        Lighting.setupForFlatItems();
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 40, 16777215);
        this.renderCouplet(graphics);
        Lighting.setupFor3DItems();
        super.render(graphics, i, i1, v);
    }

    public void onClose() {
        this.onDone();
    }

    public boolean isPauseScreen() {
        return false;
    }

    private void renderCouplet(GuiGraphics graphics) {
        graphics.pose().pushPose();
        this.offsetCouplet(graphics);
        graphics.pose().pushPose();
        this.renderCoupletBackground(graphics);
        graphics.pose().popPose();
        this.renderCoupletText(graphics);
        graphics.pose().popPose();
    }

    private void renderCoupletText(GuiGraphics graphics) {
        graphics.pose().translate(0.0F, 0.9765628F * 12, 4.0F);
        Vector3f vector3f = this.getCoupletTextScale();
        graphics.pose().scale(vector3f.x(), vector3f.y(), vector3f.z());
        int i = this.text.getColor().getTextColor();
        boolean flag = this.frame / 6 % 2 == 0;
        int j = this.coupletField.getCursorPos();
        int k = this.coupletField.getSelectionPos();
        int l = 4 * this.couplet.getTextLineHeight() / 2;
        int i1 = this.line * this.couplet.getTextLineHeight() - l;

        for(int j1 = 0; j1 < this.messages.length; ++j1) {
            String s = this.messages[j1];
            if (s != null) {
                if (this.font.isBidirectional()) {
                    s = this.font.bidirectionalShaping(s);
                }

                int k1 = -this.font.width(s) / 2;
                graphics.drawString(this.font, s, k1, j1 * this.couplet.getTextLineHeight() - l, i, false);
                if (j1 == this.line && j >= 0 && flag) {
                    int l1 = this.font.width(s.substring(0, Math.max(Math.min(j, s.length()), 0)));
                    int i2 = l1 - this.font.width(s) / 2;
                    if (j >= s.length()) {
                        graphics.drawString(this.font, "_", i2, i1, i, false);
                    }
                }
            }
        }

        for(int k3 = 0; k3 < this.messages.length; ++k3) {
            String s1 = this.messages[k3];
            if (s1 != null && k3 == this.line && j >= 0) {
                int l3 = this.font.width(s1.substring(0, Math.max(Math.min(j, s1.length()), 0)));
                int i4 = l3 - this.font.width(s1) / 2;
                if (flag && j < s1.length()) {
                    graphics.fill(i4, i1 - 1, i4 + 1, i1 + this.couplet.getTextLineHeight(), -16777216 | i);
                }

                if (k != j) {
                    int j4 = Math.min(j, k);
                    int j2 = Math.max(j, k);
                    int k2 = this.font.width(s1.substring(0, j4)) - this.font.width(s1) / 2;
                    int l2 = this.font.width(s1.substring(0, j2)) - this.font.width(s1) / 2;
                    int i3 = Math.min(k2, l2);
                    int j3 = Math.max(k2, l2);
                    graphics.fill(RenderType.guiTextHighlight(), i3, i1, j3, i1 + this.couplet.getTextLineHeight(), -16776961);
                }
            }
        }
    }

    private void onDone() {
        this.minecraft.setScreen(null);
    }

    public void setMessage(String s) {
        this.messages[this.line] = s;
        this.text = this.text.setMessage(this.line, Component.literal(s));
        this.couplet.setText(this.text, true);
    }

    public boolean keyPressed(int key, int i, int i1) {
        if (key == 265) {
            this.line = Math.max(this.line - 1, 0);
            this.coupletField.setCursorToEnd();
            return true;
        } else if (key != 264 && key != 257 && key != 335) {
            return this.coupletField.keyPressed(key) ? true : super.keyPressed(key, i, i1);
        } else {
            this.line = Math.min(this.line + 1, 6);
            this.coupletField.setCursorToEnd();
            return true;
        }
    }

    protected void offsetCouplet(GuiGraphics graphics) {
        graphics.pose().translate((float)this.width / 2.0F, 90.0F, 50.0F);
    }

    protected void renderCoupletBackground(GuiGraphics graphics) {
        if (this.coupletModel != null) {
            graphics.pose().translate(0.0F, 0.0F, 0.0F);
            graphics.pose().scale(62.500004F, 62.500004F, -62.500004F);
            VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(coupletModel.renderType(new ResourceLocation(Celebrations.MOD_ID, "textures/block/couplet.png")));
            this.coupletModel.root.render(graphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY);
        }
    }

    protected Vector3f getCoupletTextScale() {
        return TEXT_SCALE;
    }

    public void removed() {
        Celebrations.sendMessageToServer(new CoupletUpdateMessage(this.couplet.getBlockPos(), messages));
    }
}
