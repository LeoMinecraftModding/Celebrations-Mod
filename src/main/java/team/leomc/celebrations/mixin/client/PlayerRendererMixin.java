package team.leomc.celebrations.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.leomc.celebrations.client.event.CClientEvents;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
	@Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
	private void render(AbstractClientPlayer player, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
		PlayerRenderer renderer = (PlayerRenderer) (Object) this;
		Vec3 entityPos = new Vec3(Mth.lerp(partialTicks, player.xo, player.getX()), Mth.lerp(partialTicks, player.yo, player.getY()), Mth.lerp(partialTicks, player.zo, player.getZ()));
		double scale = 1;
		if (player.getAttributes().hasAttribute(Attributes.SCALE)) {
			AttributeInstance instance = player.getAttribute(Attributes.SCALE);
			if (instance != null) {
				scale = instance.getValue();
			}
		}
		for (int i = 0; i < 2; i++) {
			boolean left = i == 0;
			PoseStack stack = new PoseStack();
			stack.translate(entityPos.x, entityPos.y, entityPos.z);
			stack.mulPose(new Quaternionf().rotationY((-Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot) + 180.0F) * Mth.DEG_TO_RAD));
			stack.scale(-1, -1, 1);
			stack.translate(0, -1.5f, 0);

			(left ? renderer.getModel().leftArm : renderer.getModel().rightArm).translateAndRotate(stack);
			stack.translate(0, 0.55, 0);

			Vector4f vec = new Vector4f(0, 0, 0, 1).mul(stack.last().pose());
			Vec3 pos = new Vec3(vec.x(), vec.y(), vec.z());
			Vec3 subtract = pos.subtract(entityPos);
			(left ? CClientEvents.PLAYER_LEFT_HAND_POS : CClientEvents.PLAYER_RIGHT_HAND_POS).put(player.getId(), entityPos.add(subtract.scale(scale)));
		}
	}
}
