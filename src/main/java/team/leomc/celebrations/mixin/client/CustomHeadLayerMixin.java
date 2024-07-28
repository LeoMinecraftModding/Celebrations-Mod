package team.leomc.celebrations.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.leomc.celebrations.client.renderer.PartyHatRenderer;
import team.leomc.celebrations.entity.PartyHatWearer;
import team.leomc.celebrations.item.component.PartyHat;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.registry.CItems;
import team.leomc.celebrations.util.PartyHatUtils;

@OnlyIn(Dist.CLIENT)
@Mixin(CustomHeadLayer.class)
public abstract class CustomHeadLayerMixin<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> {
	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
	protected void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
		if (livingEntity instanceof PartyHatWearer wearer && wearer.isWearingPartyHat() && livingEntity instanceof Mob mob && !stack.is(CItems.PARTY_HAT.get())) {
			stack = PartyHatUtils.getMobPartyHatItem(mob);
		}
		if (stack.is(CItems.PARTY_HAT.get())) {
			poseStack.pushPose();
			poseStack.translate(0, 0.375f, 0);
			PartyHatRenderer.render(PartyHatRenderer.PARTY_HAT, 1f / 0.625f, false, poseStack, buffer, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1), packedLight);
			PartyHat hat = stack.get(CDataComponents.PART_HAT.get());
			if (hat != null) {
				PartyHatRenderer.render(hat.getTextureLocation(), 1f / 0.625f, true, poseStack, buffer, hat.color().getTextureDiffuseColor(), packedLight);
			}
			poseStack.popPose();
		}
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"))
	protected void renderBeforeGet(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (livingEntity instanceof PartyHatWearer wearer) {
			wearer.beforeClientGetItem();
		}
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
	protected void renderAfterGet(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (livingEntity instanceof PartyHatWearer wearer) {
			wearer.afterClientGetItem();
		}
	}
}
