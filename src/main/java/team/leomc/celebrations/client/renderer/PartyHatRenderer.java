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
	public static final ResourceLocation NEWSPAPER_HAT = Celebrations.id("textures/entity/newspaper_hat.png");

	public static void render(ResourceLocation texture, float scale, boolean overlay, PoseStack poseStack, MultiBufferSource buffer, int color, int packedLight) {
		VertexConsumer consumer = buffer.getBuffer(CRenderType.entityCutoutNoCullTriangles(texture));

		float hatRadius = overlay ? 3f / 16f + 0.001f : 3f / 16f;
		float hatBottom = overlay ? -0.001f : 0;
		float hatHeight = overlay ? 0.501f : 0.5f;
		float cubeRadius = overlay ? 0.0635f : 0.0625f;

		poseStack.pushPose();
		poseStack.scale(scale, scale, scale);
		PoseStack.Pose pose = poseStack.last();

		vertex(consumer, pose, hatRadius, hatBottom, hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(consumer, pose, -hatRadius, hatBottom, hatRadius, 1, 11f / 17f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 0.5f, 2.5f / 17f, color, packedLight);

		vertex(consumer, pose, hatRadius, hatBottom, -hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(consumer, pose, -hatRadius, hatBottom, -hatRadius, 1, 11f / 17f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 0.5f, 2.5f / 17f, color, packedLight);

		vertex(consumer, pose, hatRadius, hatBottom, hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(consumer, pose, hatRadius, hatBottom, -hatRadius, 1, 11f / 17f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 0.5f, 2.5f / 17f, color, packedLight);

		vertex(consumer, pose, -hatRadius, hatBottom, hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(consumer, pose, -hatRadius, hatBottom, -hatRadius, 1, 11f / 17f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 0.5f, 2.5f / 17f, color, packedLight);

		VertexConsumer quadsConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

		vertex(quadsConsumer, pose, hatRadius, hatBottom, hatRadius, 0, 11f / 17f, color, packedLight);
		vertex(quadsConsumer, pose, hatRadius, hatBottom, -hatRadius, 0, 1, color, packedLight);
		vertex(quadsConsumer, pose, -hatRadius, hatBottom, -hatRadius, 1, 1, color, packedLight);
		vertex(quadsConsumer, pose, -hatRadius, hatBottom, hatRadius, 1, 11f / 17f, color, packedLight);

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

	public static void renderNewspaperHat(float scale, PoseStack poseStack, MultiBufferSource buffer, int color, int packedLight) {
		VertexConsumer consumer = buffer.getBuffer(CRenderType.entityCutoutNoCullTriangles(NEWSPAPER_HAT));

		float hatRadiusX = 10.5f / 16f;
		float hatRadiusZ = 8.5f / 16f;
		float hatHeight = 0.5f;

		poseStack.pushPose();
		poseStack.scale(scale, scale, scale);
		PoseStack.Pose pose = poseStack.last();

		vertex(consumer, pose, hatRadiusX, 0, 0, 0, 9f / 16f, color, packedLight);
		vertex(consumer, pose, 0, 0, hatRadiusZ, 0.25f, 9f / 16f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 2.655f / 28f, 0.342f / 16f, color, packedLight);

		vertex(consumer, pose, hatRadiusX, 0, 0, 0.25f, 9f / 16f, color, packedLight);
		vertex(consumer, pose, 0, 0, -hatRadiusZ, 0.25f * 2, 9f / 16f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 2.655f / 28f + 0.25f, 0.342f / 16f, color, packedLight);

		vertex(consumer, pose, -hatRadiusX, 0, 0, 0.25f * 2, 9f / 16f, color, packedLight);
		vertex(consumer, pose, 0, 0, hatRadiusZ, 0.25f * 3, 9f / 16f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 2.655f / 28f + 0.25f * 2, 0.342f / 16f, color, packedLight);

		vertex(consumer, pose, -hatRadiusX, 0, 0, 0.25f * 3, 9f / 16f, color, packedLight);
		vertex(consumer, pose, 0, 0, -hatRadiusZ, 0.25f * 4, 9f / 16f, color, packedLight);
		vertex(consumer, pose, 0, hatHeight, 0, 2.655f / 28f + 0.25f * 3, 0.342f / 16f, color, packedLight);

		VertexConsumer quadsConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(NEWSPAPER_HAT));

		vertex(quadsConsumer, pose, 0, 0, hatRadiusZ, 0, 9.387f / 16f, color, packedLight);
		vertex(quadsConsumer, pose, hatRadiusX, 0, 0, 1.387f / 28f, 1, color, packedLight);
		vertex(quadsConsumer, pose, 0, 0, -hatRadiusZ, 8 / 28f, 1, color, packedLight);
		vertex(quadsConsumer, pose, -hatRadiusX, 0, 0, (7 - 0.387f) / 28f, 9.387f / 16f, color, packedLight);

		poseStack.popPose();
	}

	private static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, float x, float y, float z, float uvX, float uvY, int color, int light) {
		vertexConsumer.addVertex(pose, x, y, z).setColor(color).setUv(uvX, uvY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
	}
}
