package team.leomc.celebrations.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.entity.Balloon;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Celebrations.ID, value = Dist.CLIENT)
public class CClientEvents {
	public static final int FULL_BRIGHT = 0xf000f0;
	public static final Int2ObjectArrayMap<Vec3> PLAYER_LEFT_HAND_POS = new Int2ObjectArrayMap<>();
	public static final Int2ObjectArrayMap<Vec3> PLAYER_RIGHT_HAND_POS = new Int2ObjectArrayMap<>();
	public static final Int2IntArrayMap BALLOON_LIGHT = new Int2IntArrayMap();

	@SubscribeEvent
	private static void onRenderLevelStage(RenderLevelStageEvent event) {
		ClientLevel level = Minecraft.getInstance().level;
		EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		PoseStack stack = event.getPoseStack();
		MultiBufferSource buffer = event.getLevelRenderer().renderBuffers.bufferSource();
		Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
		Vec3 cameraPos = camera.getPosition();

		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
			PLAYER_LEFT_HAND_POS.clear();
			PLAYER_RIGHT_HAND_POS.clear();
			BALLOON_LIGHT.clear();
		}

		if (level != null && event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			stack.pushPose();
			stack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
			float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(Minecraft.getInstance().level != null && Minecraft.getInstance().level.tickRateManager().runsNormally());
			for (Entity entity : level.entitiesForRendering()) {
				if (entity instanceof Balloon balloon) {
					LivingEntity owner = balloon.getOwner();
					if (owner instanceof Player player) {
						HumanoidArm arm = balloon.isMainHand() ? player.getMainArm() : player.getMainArm().getOpposite();
						Vec3 handPos = arm == HumanoidArm.RIGHT ? PLAYER_RIGHT_HAND_POS.get(player.getId()) : PLAYER_LEFT_HAND_POS.get(player.getId());
						if (entityRenderDispatcher.options.getCameraType().isFirstPerson() && owner == Minecraft.getInstance().player) {
							int hand = owner.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
							if (!balloon.isMainHand()) {
								hand = -hand;
							}
							Vec3 vec3 = entityRenderDispatcher.camera.getNearPlane().getPointOnPlane(hand * 1.2f, -1.2f).scale(960.0 / (double) entityRenderDispatcher.options.fov().get());
							handPos = owner.getEyePosition(partialTicks).add(vec3);
						}
						if (handPos != null) {
							stack.pushPose();
							Vec3 entityPos = balloon.getPosition(partialTicks);
							stack.translate(entityPos.x, entityPos.y, entityPos.z);
							renderLeash(handPos, entityPos, BALLOON_LIGHT.getOrDefault(balloon.getId(), FULL_BRIGHT), stack, buffer);
							stack.popPose();
						}
					}
				}
			}
			stack.popPose();
		}
	}

	private static void renderLeash(Vec3 from, Vec3 to, int light, PoseStack poseStack, MultiBufferSource bufferSource) {
		poseStack.pushPose();
		double d3 = to.x;
		double d4 = to.y;
		double d5 = to.z;
		float f = (float) (from.x - d3);
		float f1 = (float) (from.y - d4);
		float f2 = (float) (from.z - d5);
		VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.leash());
		Matrix4f matrix4f = poseStack.last().pose();
		float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
		float f5 = f2 * f4;
		float f6 = f * f4;

		int j1;
		for (j1 = 0; j1 <= 24; ++j1) {
			addVertexPair(vertexconsumer, matrix4f, f, f1, f2, light, 0.025F, 0.025F, f5, f6, j1, false);
		}

		for (j1 = 24; j1 >= 0; --j1) {
			addVertexPair(vertexconsumer, matrix4f, f, f1, f2, light, 0.025F, 0.0F, f5, f6, j1, true);
		}

		poseStack.popPose();
	}

	private static void addVertexPair(VertexConsumer buffer, Matrix4f pose, float startX, float startY, float startZ, int light, float yOffset, float dy, float dx, float dz, int index, boolean reverse) {
		float f = (float) index / 24.0F;
		float f1 = index % 2 == (reverse ? 1 : 0) ? 0.7F : 1.0F;
		float f2 = 0.5F * f1;
		float f3 = 0.4F * f1;
		float f4 = 0.3F * f1;
		float f5 = startX * f;
		float f6 = startY > 0.0F ? startY * f * f : startY - startY * (1.0F - f) * (1.0F - f);
		float f7 = startZ * f;
		buffer.addVertex(pose, f5 - dx, f6 + dy, f7 + dz).setColor(f2, f3, f4, 1.0F).setLight(light);
		buffer.addVertex(pose, f5 + dx, f6 + yOffset - dy, f7 - dz).setColor(f2, f3, f4, 1.0F).setLight(light);
	}
}
