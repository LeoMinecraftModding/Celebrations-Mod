package team.leomc.celebrations.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.client.CRenderType;

@OnlyIn(Dist.CLIENT)
public class PartyHatRenderer {
	public static final ResourceLocation PARTY_HAT = Celebrations.id("textures/entity/party_hat.png");

	public static void render(ResourceLocation texture, boolean overlay, PoseStack poseStack, MultiBufferSource buffer, int color, int packedLight) {
		VertexConsumer consumer = buffer.getBuffer(CRenderType.entityCutoutNoCullTriangles(texture));

		PoseStack.Pose pose = poseStack.last();
		float hatRadius = overlay ? 3.0001f / 16f : 3f / 16f;
		float hatHeight = 0.5f;
		float cubeRadius = overlay ? 0.0626f : 0.0625f;

		vertex(consumer, pose, hatRadius, 0, hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(consumer, pose, -hatRadius, 0, hatRadius, 1, 11f / 17f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 0.5f, 2.5f / 17f, color, packedLight);

		vertex(consumer, pose, hatRadius, 0, -hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(consumer, pose, -hatRadius, 0, -hatRadius, 1, 11f / 17f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 0.5f, 2.5f / 17f, color, packedLight);

		vertex(consumer, pose, hatRadius, 0, hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(consumer, pose, hatRadius, 0, -hatRadius, 1, 11f / 17f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 0.5f, 2.5f / 17f, color, packedLight);

		vertex(consumer, pose, -hatRadius, 0, hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(consumer, pose, -hatRadius, 0, -hatRadius, 1, 11f / 17f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 0.5f, 2.5f / 17f, color, packedLight);

		poseStack.pushPose();
		VertexConsumer quadsConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

		vertex(quadsConsumer, pose, hatRadius, 0, hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, hatRadius, 0, -hatRadius, 0, 1, color, packedLight);
		vertex(quadsConsumer, pose, -hatRadius, 0, -hatRadius, 1, 1, color, packedLight);
		vertex(quadsConsumer, pose, -hatRadius, 0, hatRadius, 1, 11f / 17f, color, packedLight);

		poseStack.translate(0, 0.5f, 0);
		pose = poseStack.last();

		vertex(quadsConsumer, pose, cubeRadius, -cubeRadius, cubeRadius, 2f / 6f, 0, color, packedLight);
		vertex(quadsConsumer, pose, cubeRadius, -cubeRadius, -cubeRadius, 2f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, -cubeRadius, -cubeRadius, 4f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, -cubeRadius, cubeRadius, 4f / 6f, 0, color, packedLight);

		vertex(quadsConsumer, pose, cubeRadius, cubeRadius, cubeRadius, 2f / 6f, 0, color, packedLight);
		vertex(quadsConsumer, pose, cubeRadius, cubeRadius, -cubeRadius, 2f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, cubeRadius, -cubeRadius, 4f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, cubeRadius, cubeRadius, 4f / 6f, 0, color, packedLight);

		vertex(quadsConsumer, pose, -cubeRadius, cubeRadius, cubeRadius, 2f / 6f, 0, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, cubeRadius, -cubeRadius, 2f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, -cubeRadius, -cubeRadius, 4f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, -cubeRadius, cubeRadius, 4f / 6f, 0, color, packedLight);

		vertex(quadsConsumer, pose, cubeRadius, cubeRadius, cubeRadius, 2f / 6f, 0, color, packedLight);
		vertex(quadsConsumer, pose, cubeRadius, cubeRadius, -cubeRadius, 2f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, cubeRadius, -cubeRadius, -cubeRadius, 4f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, cubeRadius, -cubeRadius, cubeRadius, 4f / 6f, 0, color, packedLight);

		vertex(quadsConsumer, pose, cubeRadius, cubeRadius, -cubeRadius, 2f / 6f, 0, color, packedLight);
		vertex(quadsConsumer, pose, cubeRadius, -cubeRadius, -cubeRadius, 2f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, -cubeRadius, -cubeRadius, 4f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, cubeRadius, -cubeRadius, 4f / 6f, 0, color, packedLight);

		vertex(quadsConsumer, pose, cubeRadius, cubeRadius, cubeRadius, 2f / 6f, 0, color, packedLight);
		vertex(quadsConsumer, pose, cubeRadius, -cubeRadius, cubeRadius, 2f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, -cubeRadius, cubeRadius, 4f / 6f, 2f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, -cubeRadius, cubeRadius, cubeRadius, 4f / 6f, 0, color, packedLight);

		poseStack.popPose();
	}

	private static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, float x, float y, float z, float uvX, float uvY, int color, int light) {
		vertexConsumer.addVertex(pose, x, y, z).setColor(color).setUv(uvX, uvY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
	}
}
