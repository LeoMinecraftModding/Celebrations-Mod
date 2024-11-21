package team.leomc.celebrations.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.entity.FuSticker;
import team.leomc.celebrations.registry.CItems;

@OnlyIn(Dist.CLIENT)
public class FuStickerRenderer extends EntityRenderer<FuSticker> {
	private static final ResourceLocation FU = Celebrations.id("textures/entity/fu_sticker.png");
	private static final ResourceLocation INVERTED_FU = Celebrations.id("textures/entity/inverted_fu_sticker.png");
	private static final ResourceLocation GOLDEN_FU = Celebrations.id("textures/entity/golden_fu_sticker.png");
	private static final ResourceLocation INVERTED_GOLDEN_FU = Celebrations.id("textures/entity/inverted_golden_fu_sticker.png");

	public FuStickerRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(FuSticker entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
		poseStack.pushPose();
		poseStack.translate(0.0, -0.5, 0.0);

		BlockState blockState = entity.level().getBlockState(entity.getAttachedPos());
		if (blockState.getBlock() instanceof DoorBlock) {
			Direction facing = blockState.getValue(DoorBlock.FACING);
			boolean isOpen = blockState.getValue(DoorBlock.OPEN);
			DoorHingeSide hingeSide = blockState.getValue(DoorBlock.HINGE);

			switch (facing) {
				case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
				case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
				case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
				default -> {}
			}

			if (isOpen) {
				float openRotation = (hingeSide == DoorHingeSide.LEFT) ? 90.0F : -90.0F;
				poseStack.mulPose(Axis.YP.rotationDegrees(openRotation));
			}
		}

		VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(entity)));
		PoseStack.Pose pose = poseStack.last();
		Matrix4f matrix = pose.pose();

		int lightLevel = isGoldFu(entity) ? 240 : light;

		buffer.addVertex(matrix, -0.5f, 0.0f, 0.0f).setColor(255, 255, 255, 255).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightLevel).setNormal(pose, 0f, 0f, 1f);
		buffer.addVertex(matrix, 0.5f, 0.0f, 0.0f).setColor(255, 255, 255, 255).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightLevel).setNormal(pose, 0f, 0f, 1f);
		buffer.addVertex(matrix, 0.5f, 1.0f, 0.0f).setColor(255, 255, 255, 255).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightLevel).setNormal(pose, 0f, 0f, 1f);
		buffer.addVertex(matrix, -0.5f, 1.0f, 0.0f).setColor(255, 255, 255, 255).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightLevel).setNormal(pose, 0f, 0f, 1f);
		poseStack.popPose();
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
	}



	@Override
	public ResourceLocation getTextureLocation(FuSticker fuSticker) {
		ItemStack fu = fuSticker.getItem();
		return fu.is(CItems.FU_STICKER.get()) ? FU : fu.is(CItems.INVERTED_FU_STICKER.get()) ? INVERTED_FU : fu.is(CItems.GOLDEN_FU_STICKER.get()) ? GOLDEN_FU : INVERTED_GOLDEN_FU;
	}

	public Boolean isGoldFu(FuSticker fuSticker) {
		ItemStack fu = fuSticker.getItem();
		return fu.is(CItems.GOLDEN_FU_STICKER.get()) || fu.is(CItems.INVERTED_GOLDEN_FU_STICKER.get());
	}
}
