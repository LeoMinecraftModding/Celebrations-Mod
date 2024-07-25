package team.leomc.celebrations.client.gui.screen;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.entity.CoupletBlockEntity;
import team.leomc.celebrations.client.renderer.CoupletRenderer;
import team.leomc.celebrations.network.UpdateSignLikeTextPayload;
import team.leomc.celebrations.util.SignLikeText;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class CoupletEditScreen extends SignLikeEditScreen {
	@Nullable
	private CoupletRenderer.CoupletModel coupletModel;

	public CoupletEditScreen(CoupletBlockEntity entity) {
		super(Component.translatable("tooltip." + Celebrations.ID + ".edit_couplet"), entity);
	}

	@Override
	protected void init() {
		super.init();
		this.coupletModel = CoupletRenderer.createCoupletModel(this.minecraft.getEntityModels());
	}

	@Override
	protected boolean isLineValid(String string) {
		return string.length() <= 1;
	}

	@Override
	protected void renderSignLikeBackground(GuiGraphics graphics) {
		if (this.coupletModel != null) {
			graphics.pose().translate(0.0F, 0.0F, 0.0F);
			graphics.pose().scale(62.500004F, 62.500004F, -62.500004F);
			VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(coupletModel.renderType(Celebrations.id("textures/block/couplet.png")));
			this.coupletModel.root.render(graphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY);
		}
	}

	@Override
	protected void offsetSignLikeText(GuiGraphics graphics) {
		graphics.pose().translate(0.0F, 11.7F, 4.0F);
	}

	@Override
	public void removed() {
		PacketDistributor.sendToServer(new UpdateSignLikeTextPayload(signLike.getBlockPos(), new SignLikeText(Arrays.stream(messages).map(s -> (Component) Component.literal(s)).toList(), signLike.getText().color(), signLike.getText().glow())));
	}
}
