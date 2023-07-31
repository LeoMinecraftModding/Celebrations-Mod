package cn.leolezury.celebrations.client.gui.screens;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.client.renderer.HorizontalScrollRenderer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class HorizontalScrollEditScreen extends AbstractSignEditScreen {
    private static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);
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

    protected void offsetSign(GuiGraphics graphics, BlockState state) {
        super.offsetSign(graphics, state);
        graphics.pose().translate(0.0F, 35.0F, 0.0F);
    }

    protected void renderSignBackground(GuiGraphics graphics, BlockState state) {
        if (this.scrollModel != null) {
            graphics.pose().translate(0.0F, 31.0F, 0.0F);
            graphics.pose().scale(62.500004F, 62.500004F, -62.500004F);
            VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(scrollModel.renderType(new ResourceLocation(Celebrations.MOD_ID, "textures/block/horizontal_scroll.png")));
            this.scrollModel.root.render(graphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY);
        }
    }

    protected Vector3f getSignTextScale() {
        return TEXT_SCALE;
    }
}
