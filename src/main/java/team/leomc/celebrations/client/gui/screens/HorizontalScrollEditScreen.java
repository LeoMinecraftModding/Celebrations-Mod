package team.leomc.celebrations.client.gui.screens;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.entity.HorizontalScrollBlockEntity;
import team.leomc.celebrations.client.renderer.HorizontalScrollRenderer;
import team.leomc.celebrations.network.UpdateSignLikeTextPayload;
import team.leomc.celebrations.util.SignLikeText;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class HorizontalScrollEditScreen extends SignLikeEditScreen {
	@Nullable
	private HorizontalScrollRenderer.ScrollModel scrollModel;

	public HorizontalScrollEditScreen(HorizontalScrollBlockEntity entity) {
		super(Component.translatable("tooltip." + Celebrations.ID + ".edit_horizontal_scroll"), entity);
	}

	protected void init() {
		super.init();
		this.scrollModel = HorizontalScrollRenderer.createScrollModel(this.minecraft.getEntityModels());
	}

	@Override
	protected void renderSignLikeBackground(GuiGraphics graphics) {
		if (this.scrollModel != null) {
			graphics.pose().translate(0.0F, -22.0F, 0.0F);
			graphics.pose().scale(62.500004F, 62.500004F, -62.500004F);
			VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(scrollModel.renderType(Celebrations.id("textures/block/horizontal_scroll.png")));
			this.scrollModel.root.render(graphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY);
		}
	}

	@Override
	protected void offsetSignLikeText(GuiGraphics graphics) {
		graphics.pose().translate(0.0F, 62.5F, 4.0F);
	}

	@Override
	public void removed() {
		PacketDistributor.sendToServer(new UpdateSignLikeTextPayload(signLike.getBlockPos(), new SignLikeText(Arrays.stream(messages).map(s -> (Component) Component.literal(s)).toList(), signLike.getText().color(), signLike.getText().glow())));
	}
}
