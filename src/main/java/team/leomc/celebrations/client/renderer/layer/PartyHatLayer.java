package team.leomc.celebrations.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.leomc.celebrations.client.renderer.PartyHatRenderer;
import team.leomc.celebrations.entity.PartyHatWearer;
import team.leomc.celebrations.item.component.PartyHat;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.registry.CItems;
import team.leomc.celebrations.util.PartyHatUtils;

@OnlyIn(Dist.CLIENT)
public class PartyHatLayer<T extends Mob, M extends EntityModel<T> & PartyHatParent> extends RenderLayer<T, M> {
	public PartyHatLayer(RenderLayerParent<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack stack = mob.getItemBySlot(EquipmentSlot.HEAD);
		if (!stack.is(CItems.PARTY_HAT.get()) && !stack.isEmpty()) {
			return;
		}
		poseStack.pushPose();
		getParentModel().translate(poseStack);
		if (mob instanceof PartyHatWearer wearer && wearer.isWearingPartyHat() && stack.isEmpty()) {
			stack = PartyHatUtils.getMobPartyHatItem(mob);
		}
		if (stack.is(CItems.PARTY_HAT.get())) {
			poseStack.pushPose();
			poseStack.translate(0, 0.375f, 0);
			PartyHatRenderer.render(PartyHatRenderer.PARTY_HAT, 1, false, poseStack, buffer, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1), packedLight);
			PartyHat hat = stack.get(CDataComponents.PART_HAT.get());
			if (hat != null) {
				PartyHatRenderer.render(hat.getTextureLocation(), 1, true, poseStack, buffer, hat.color().getTextureDiffuseColor(), packedLight);
			}
			poseStack.popPose();
		}
		poseStack.popPose();
	}
}
