package cn.leolezury.celebrations.client.gui.screens;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.client.renderer.HorizontalScrollRenderer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class HorizontalScrollEditScreen extends AbstractSignEditScreen {
    private static final float scale = 1.5f;
    private static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F * scale, 0.9765628F * scale, 0.9765628F * scale);
    @Nullable
    private HorizontalScrollRenderer.ScrollModel scrollModel;

    public HorizontalScrollEditScreen(SignBlockEntity entity, boolean b, boolean b1) {
        super(entity, b, b1, Component.translatable("tooltip." + Celebrations.MOD_ID + ".edit_horizontal_scroll"));
    }

    protected void init() {
        super.init();
        this.scrollModel = HorizontalScrollRenderer.createScrollModel(this.minecraft.getEntityModels());
        this.signField = new TextFieldHelper(() -> {
            return this.messages[this.line];
        }, this::setMessage, TextFieldHelper.createClipboardGetter(this.minecraft), TextFieldHelper.createClipboardSetter(this.minecraft), (s) -> {
            return s.length() <= 4;
        });
    }

    @Override
    public boolean keyPressed(int key, int i, int i1) {
        if (key != 265 && key != 264 && key != 257 && key != 335) {
            return this.signField.keyPressed(key) ? true : super.keyPressed(key, i, i1);
        }
        return false;
    }

    protected void renderSignBackground(GuiGraphics graphics, BlockState state) {
        if (this.scrollModel != null) {
            graphics.pose().translate(0.0F, -22.0F, 0.0F);
            graphics.pose().scale(62.500004F, 62.500004F, -62.500004F);
            VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(scrollModel.renderType(new ResourceLocation(Celebrations.MOD_ID, "textures/block/horizontal_scroll.png")));
            this.scrollModel.root.render(graphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY);
        }
    }

    @Override
    public void renderSignText(GuiGraphics graphics) {
        graphics.pose().translate(0.0F, 62.5F, 4.0F);
        Vector3f vector3f = this.getSignTextScale();
        graphics.pose().scale(vector3f.x(), vector3f.y(), vector3f.z());
        int i = this.text.getColor().getTextColor();
        boolean flag = this.frame / 6 % 2 == 0;
        int j = this.signField.getCursorPos();
        int k = this.signField.getSelectionPos();
        int l = 4 * this.sign.getTextLineHeight() / 2;
        int i1 = this.line * this.sign.getTextLineHeight() - l;

        for(int j1 = 0; j1 < this.messages.length; ++j1) {
            String s = this.messages[j1];
            if (s != null) {
                if (this.font.isBidirectional()) {
                    s = this.font.bidirectionalShaping(s);
                }

                int k1 = -this.font.width(s) / 2;
                graphics.drawString(this.font, s, k1, j1 * this.sign.getTextLineHeight() - l, i, false);
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
                    graphics.fill(i4, i1 - 1, i4 + 1, i1 + this.sign.getTextLineHeight(), -16777216 | i);
                }

                if (k != j) {
                    int j4 = Math.min(j, k);
                    int j2 = Math.max(j, k);
                    int k2 = this.font.width(s1.substring(0, j4)) - this.font.width(s1) / 2;
                    int l2 = this.font.width(s1.substring(0, j2)) - this.font.width(s1) / 2;
                    int i3 = Math.min(k2, l2);
                    int j3 = Math.max(k2, l2);
                    graphics.fill(RenderType.guiTextHighlight(), i3, i1, j3, i1 + this.sign.getTextLineHeight(), -16776961);
                }
            }
        }

    }

    protected Vector3f getSignTextScale() {
        return TEXT_SCALE;
    }
}
