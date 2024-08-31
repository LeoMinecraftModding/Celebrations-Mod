package team.leomc.celebrations.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.client.event.CClientEvents;
import team.leomc.celebrations.client.model.entity.BalloonModel;
import team.leomc.celebrations.entity.Balloon;

@OnlyIn(Dist.CLIENT)
public class BalloonRenderer extends EntityRenderer<Balloon> {
	private static final ResourceLocation ENTITY_TEXTURE = Celebrations.id("textures/entity/balloon.png");
	private final BalloonModel<Balloon> model;

	public BalloonRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new BalloonModel<>(context.bakeLayer(BalloonModel.LAYER_LOCATION));
	}

	@Override
	public void render(Balloon entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.translate(0.0F, -1.5F, 0.0F);
		RenderType renderType = this.model.renderType(getTextureLocation(entity));
		VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
		this.model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, entity.getRenderColor());
		poseStack.popPose();
		CClientEvents.BALLOON_LIGHT.put(entity.getId(), light);
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
	}

	@Override
	public ResourceLocation getTextureLocation(Balloon entity) {
		return ENTITY_TEXTURE;
	}
}
