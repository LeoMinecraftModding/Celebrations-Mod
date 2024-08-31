package team.leomc.celebrations.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.leomc.celebrations.entity.BalloonOwner;
import team.leomc.celebrations.registry.CItems;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
	protected void renderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int seed, CallbackInfo ci) {
		if (displayContext == ItemDisplayContext.HEAD && itemStack.is(CItems.PARTY_HAT.get())) {
			ci.cancel();
		}
		if (entity instanceof BalloonOwner owner) {
			if (owner.getMainHandBalloon() != null) {
				if (entity.getMainArm() == HumanoidArm.RIGHT && (displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
					ci.cancel();
				}
				if (entity.getMainArm() == HumanoidArm.LEFT && (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND)) {
					ci.cancel();
				}
			}
			if (owner.getOffhandBalloon() != null) {
				if (entity.getMainArm() == HumanoidArm.LEFT && (displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
					ci.cancel();
				}
				if (entity.getMainArm() == HumanoidArm.RIGHT && (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND)) {
					ci.cancel();
				}
			}
		}
	}
}
